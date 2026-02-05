package org.fmm.teleworking.ui

import android.content.Context
import android.util.Log
import androidx.annotation.AttrRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.fmm.teleworking.R
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality
import java.time.YearMonth

private val COLOR_TELEWORK = R.color.yellow
private val COLOR_PRESENTIAL = R.color.blue
private val COLOR_UNASSIGNED = Color.White
private val COLOR_FESTIVE = R.color.darkgrey
private val COLOR_WEEKEND = R.color.grey
private val CELL_BORDER = R.color.other

private enum class EditMode { NONE, TELEWORK, PRESENTIAL }

@Composable
fun CalendarMonthEditor (
    year: Int,
    month: Int,
    days: List<DayDto>,
    modifier: Modifier = Modifier,
    onApplyModality: (dateIso: LocalDate, modality: Modality) -> Unit
){
    // Which edit mode is active (T/P/None)
    var editMode by remember { mutableStateOf(EditMode.NONE) }
    Column(modifier = modifier.padding(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Edit", modifier = Modifier.padding(end = 8.dp),
                style=MaterialTheme.typography.labelMedium)
            ToggleButton(label="T", selected=editMode == EditMode.TELEWORK, onClick =  {
                editMode = if (editMode == EditMode.TELEWORK) EditMode.NONE else EditMode.TELEWORK
            })
            Spacer(modifier = Modifier.width(8.dp))
            ToggleButton(label="P", selected=editMode == EditMode.PRESENTIAL, onClick =  {
                editMode = if (editMode == EditMode.PRESENTIAL) EditMode.NONE else EditMode.PRESENTIAL
            })
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "${YearMonth.of(year, month).month.name.lowercase()
                    .replaceFirstChar { it.uppercase() }} $year",
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Build calendar matrix: list of weeks, each week is list of LocalDate?
        // (null for leading/trailing blanks)
        val weeks = remember(year, month) {
            buildMonthMatrix(year, month)
        }

        // Map dates to DayDto for quick lookup
        val dayMap = remember(days) { days.associateBy { it.date }}

        // Map to store cell bounds (for drag)
        val cellBounds = remember { mutableStateMapOf<LocalDate, Rect>() } // date -> Rect

        // Pointer input on whole grid to track drag/tap and compute which day is under pointer
        val density = LocalDensity.current

        Column(
            modifier = Modifier.fillMaxSize()
                .wrapContentHeight()
                .pointerInput(editMode) {
                    // only active if editMode != NONE
                    // detect gestures and apply modality accordingly
                    if (editMode == EditMode.NONE) return@pointerInput
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        // process initial position
                        processPointerPosition(down.position, cellBounds, editMode,onApplyModality)
                        //  then track moves until pointer up
                        var pointerUp = false
                        while (!pointerUp) {
                            val event = awaitPointerEvent()
                            // handle all changes in event
                            event.changes.forEach { pc ->
                                if (pc.pressed)
                                    processPointerPosition(pc.position, cellBounds, editMode, onApplyModality)
                                else
                                    pointerUp = true

                            }
                        }
                    }
                }

        ) {
            // Weekday headers (Mon..Sun). Choose MOnday start; change if needed.
            Row(modifier = Modifier.fillMaxWidth()) {
                val headers = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                for (h in headers) {
                    Box(modifier = Modifier.weight(1f).padding(4.dp),
                        contentAlignment =Alignment.Center){
                        Text(h, fontWeight = FontWeight.Bold, style=MaterialTheme.typography
                            .bodyMedium,
                            textAlign =
                            TextAlign.Center)
                    }
                }
            }

            // Calendar grid: one row per week
            for (week in weeks) {
                Log.d("[FMM]", "Semana: $week")
                println("Hola, amigo")
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (date in week) {
                        Log.d("[FMM]", "Día: $date")
                        Box(
                            modifier= Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                        ) {
                            if (date == null) {
                                // empty cell
                                Box(modifier = Modifier.fillMaxSize().border(
                                    width = 1.dp,
                                    color= colorResource(CELL_BORDER),
                                    shape = RoundedCornerShape(6.dp)
                                ))
                            } else {
                                val iso = date.toString()
                                val dto = dayMap[date]
                                DayCell(
                                    date = date,
                                    dto = dto,
                                    onPositioned = { rect ->
                                        // store bounds in root coordinates
                                        cellBounds[date] = rect
                                    },
                                    onTap = {
                                        // apply mode if allowed
                                        if (editMode != EditMode.NONE) {
                                            val modality = if (editMode == EditMode.TELEWORK)
                                                Modality.TELEWORK else Modality.PRESENTIAL
                                            if (!isNonEditableDay(dto)) {
                                                onApplyModality(date, modality)
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/* Small toggle pill button */
@Composable
private fun ToggleButton(label: String,  selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick=onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color.LightGray else Color.White
            //,
            //contentColor= if (selected)
        ),
        modifier = Modifier.size(width = 48.dp, height = 36.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

/**
 * Day cell: draws colored background based on modality / weekend / festive
 */
@Composable
private fun DayCell(
    date: LocalDate,
    dto: DayDto?,
    onPositioned: (Rect) -> Unit,
    onTap: () -> Unit
) {
    // Determine color
    val bg = when {
        dto?.isFestive() == true -> colorResource(COLOR_FESTIVE)
        DayDto.isWeekend(date) -> colorResource(COLOR_WEEKEND)
        dto?.isTelework() == true -> colorResource(COLOR_TELEWORK)
        dto?.isPresential() == true  -> colorResource(COLOR_PRESENTIAL)
        else -> { COLOR_UNASSIGNED}
    }

    // Outline if unassigned to be readable on white
    val borderColor = if (bg == COLOR_UNASSIGNED) colorResource(CELL_BORDER) else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .background(color=bg, shape = RoundedCornerShape(6.dp))
            .onGloballyPositioned { coords ->
                // bounds in window/root: record as Rect (left, top, right, bottom) in px
                val pos = coords.boundsInRoot()
                onPositioned(Rect(pos.left, pos.top, pos.right, pos.bottom))
            }
            .padding(6.dp)
            .pointerInput(Unit) {
                // simple tap handler per cell (so quick taps also work
                awaitEachGesture {
                    val down = awaitFirstDown()
                    // treat as tap if released without significant move
                    val up = awaitPointerEvent()
                    if (up.changes.all { !it.pressed }) {
                        onTap()
                    }
                }
            }
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text =  date.dayOfMonth.toString(),
                style= MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
            // small modality label
            Text(
                text =  dto?.modality?.name ?: "",
                style= MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.End),
                maxLines = 1
            )

        }
    }
}

private fun isNonEditableDay(dto: DayDto?): Boolean {
    if (dto == null)
        return false
//    return dto.isFestive() || dto.isWeekend()
    return dto.isWeekend()
}

/**
 * Process pointer prositions (px) against known cellBounds to apply modality
 */
private fun processPointerPosition(
    position: Offset,
    cellBounds: Map<LocalDate, Rect>,
    editMode: EditMode,
    onApply: (LocalDate, Modality) -> Unit
    ) {
    // Find first key whose rect contains the pointer
    val hit = cellBounds.entries.firstOrNull {(_,rect)->
        rect.contains(position)
    }
    if (hit != null) {
        val (iso,_) = hit
        val modality =
            if (editMode == EditMode.TELEWORK) Modality.TELEWORK
            else Modality.PRESENTIAL
        // apply directly (caller should ignore if non-editable)
        onApply(iso, modality)
    }
}

/**
 * Build matrix of weeks for given year/month
 * Returns List of 6 weeks (some rows may have all nulls at tail)
 * Each week is List<LocalDate?> size 7 (Mon..Sun)
 */
private fun buildMonthMatrix(year: Int, month: Int): List<List<LocalDate?>> {
    val ym = YearMonth.of(year, month)
    val firstJava = ym.atDay(1)

    //val first: LocalDate = LocalDate (firstJava.year, firstJava.monthValue, firstJava.dayOfMonth)
    val first: LocalDate = firstJava.toKxLocalDate()

    // We want weeks starting Monday
    val firstWeekDay = first.dayOfWeek
    // convert DayOfWeek (Mon=1.. Sun=7) -> shift count (Mon->0..Sun->6)
    val shift = (firstWeekDay.value + 6) % 7
    var cursor = first.minus(shift.toLong(), DateTimeUnit.DAY)

    val weeks = mutableListOf<MutableList<LocalDate?>>()

    repeat(6) {
        val week = mutableListOf<LocalDate?>()
        repeat(7) {
            week.add(cursor)
            cursor = cursor.plus(1, DateTimeUnit.DAY)
        }
        weeks.add(week)
    }

    // convert dates not in the target month to null
    for (r in weeks.indices) {
        for (c in weeks[r].indices) {
            val d = weeks[r][c]!!
            if (d.monthNumber != month) {
                weeks[r][c] = null
            }
        }
    }
    return weeks
}

@Preview(showBackground = true, widthDp =360, heightDp = 640)
@Composable
fun MainScreenCalendarPreview() {
    val sampleDays = listOf(
        DayDto(LocalDate.parse("2026-01-01"), Modality.FESTIVE),
        DayDto(LocalDate.parse("2026-01-02"), Modality.TELEWORK),
        DayDto(LocalDate.parse("2026-01-03"), Modality.WEEKEND),
        DayDto(LocalDate.parse("2026-01-04"), Modality.WEEKEND),
        DayDto(LocalDate.parse("2026-01-05"), Modality.HOLIDAY),
        DayDto(LocalDate.parse("2026-01-06"), Modality.FESTIVE),
        DayDto(LocalDate.parse("2026-01-07"), Modality.PRESENTIAL),
        DayDto(LocalDate.parse("2026-01-08"), Modality.TELEWORK),
        DayDto(LocalDate.parse("2026-01-09"), Modality.PRESENTIAL),
        DayDto(LocalDate.parse("2026-01-10"), Modality.WEEKEND),
        DayDto(LocalDate.parse("2026-01-11"), Modality.WEEKEND),
    )
    Surface {
    CalendarMonthEditor(
        year =2026,
        month = 1,
        days = sampleDays,
        onApplyModality = { dateIso, modality -> }
    )}
}

//fun java.time.LocalDate.toKxLocalDate(): LocalDate = LocalDate(year, monthValue, dayOfMonth)


private fun Context.getColorFromAttr(@AttrRes attrColor: Int): Int {
    val typedArray = theme.obtainStyledAttributes(intArrayOf(attrColor))
    val textColor = typedArray.getColor(0,0)
    typedArray.recycle()
    return textColor
}
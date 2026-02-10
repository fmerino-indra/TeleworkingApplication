package org.fmm.teleworking.ui.calendar

import android.content.Context
import androidx.annotation.AttrRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality
import org.fmm.teleworking.domain.model.stats.MonthStatsDto
import org.fmm.teleworking.ui.UiState
import org.fmm.teleworking.ui.colors.CELL_BORDER
import org.fmm.teleworking.ui.colors.COLOR_FESTIVE
import org.fmm.teleworking.ui.colors.COLOR_HOLIDAY
import org.fmm.teleworking.ui.colors.COLOR_PRESENTIAL
import org.fmm.teleworking.ui.colors.COLOR_TELEWORK
import org.fmm.teleworking.ui.colors.COLOR_TRAVEL
import org.fmm.teleworking.ui.colors.COLOR_UNASSIGNED
import org.fmm.teleworking.ui.colors.COLOR_WEEKEND
import java.time.YearMonth

enum class EditMode2 { NONE, TELEWORK, PRESENTIAL, TRAVEL, HOLIDAY, FESTIVE }


@Composable
fun MainScreenMonth() {
    val viewModel: CalendarViewModel = hiltViewModel<CalendarViewModel>()
    /* Dates */

    val today = remember {  Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    var currentYear by remember { mutableIntStateOf(today.year) }
    var currentMonth by remember { mutableIntStateOf(today.monthNumber) }

    var isPreviousOpened: Boolean by remember {
        mutableStateOf (viewModel.isYearOpened(currentYear-1))
    }
    var isNextOpened: Boolean by remember {
        mutableStateOf (viewModel.isYearOpened(currentYear+1))
    }


    // year input state (shared between screens)
    var yearText by remember {
        mutableStateOf(currentYear.toString())
    }


    // when screen first composed: Load current month automatically
    LaunchedEffect(Unit) {
        // ensure yearText set to current year
        yearText = currentYear.toString()

        // open year (create calendar) optionally - comment if you don't want auto-open
        // viewModel.openYear(currentYear)

        viewModel.loadMonth(currentYear, currentMonth)
    }


    // Collect days state from ViewModel
    val uiState by viewModel.uiState.collectAsState()

    val monthDto =
        when (uiState) {
            is UiState.Loading -> {
                val aux = MonthStatsDto.emptyMonthDto()
                aux
            }
            is UiState.Idle -> MonthStatsDto.emptyMonthDto()
            is UiState.Success -> (uiState as UiState.Success).monthDto
        }

    MainScreenCalendarMonthEditor(
        year =  currentYear,
        month = currentMonth,
        monthDto = monthDto as MonthStatsDto,
        isPreviousNavigable = (currentMonth > 1) || isPreviousOpened
        ,
        isNextNavigable = (currentMonth < 12) || isNextOpened,
        onApplyModality = { date, modality ->
            viewModel.setModality(date, modality)
        },
        onPrevious = {
            if (currentMonth > 1) {
                currentMonth--
                viewModel.loadMonth(year = currentYear, month = currentMonth)
            } else {
                if (isPreviousOpened) {
                    currentMonth = 12
                    currentYear--
                    isPreviousOpened = viewModel.isYearOpened(currentYear)
                    isNextOpened = true
                    viewModel.loadMonth(year = currentYear, month = currentMonth)
                }
            }
        },
        onNext = {
            if (currentMonth < 12) {
                currentMonth++
                viewModel.loadMonth(year = currentYear, month = currentMonth)
            } else {
                if (isNextOpened) {
                    currentMonth = 1
                    currentYear++
                    isPreviousOpened = true
                    isNextOpened = viewModel.isYearOpened(currentYear)
                    viewModel.loadMonth(year = currentYear, month = currentMonth)
                }
            }
        }
    )
}

@Composable
fun MainScreenCalendarMonthEditor (
    year: Int,
    month: Int,
    monthDto: MonthStatsDto,
    isPreviousNavigable: Boolean,
    isNextNavigable: Boolean,
    modifier: Modifier = Modifier,
    onApplyModality: (date: LocalDate, modality: Modality) -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
){
    //val vm = hiltViewModel<CalendarViewModel>()

    // Which edit mode is active (T/P/I/None)
    var editMode: EditMode2 by remember { mutableStateOf(EditMode2.NONE) }




    Column(modifier = modifier.padding(8.dp)) {
        Row (modifier = modifier
            .align(Alignment.CenterHorizontally)
            .background(COLOR_WEEKEND)){
            IconButton(
                enabled = isPreviousNavigable,
                onClick = onPrevious
            ) {
                Icon(
                    imageVector = Icons.Default.FastRewind,
                    contentDescription = "Previous month",
                    tint =  Color.White,
                    modifier = Modifier.padding(6.dp),
                )
            }

            Text(
                text = "${
                    YearMonth.of(year, month).month.name.lowercase()
                        .replaceFirstChar { it.uppercase() }
                } $year",
                style = MaterialTheme.typography.labelMedium,
//                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )
            IconButton(
                enabled = isNextNavigable,
                onClick = onNext
            ) {
                Icon(
                    imageVector = Icons.Default.FastForward,
                    contentDescription = "Previous month",
                    tint = Color.White,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ModeSelector2(label="T",
                icon = Icons.Default.Work,
                color=COLOR_TELEWORK,
                selected=editMode == EditMode2.TELEWORK,
                onClick = {
                    editMode = if (editMode == EditMode2.TELEWORK) EditMode2.NONE else EditMode2.TELEWORK
                })
            Spacer(modifier = Modifier.width(6.dp))
            ModeSelector2(label="P",
                icon = Icons.Default.Home,
                color=COLOR_PRESENTIAL,
                selected=editMode == EditMode2.PRESENTIAL,
                onClick = {
                    editMode = if (editMode == EditMode2.PRESENTIAL) EditMode2.NONE else EditMode2.PRESENTIAL
                })
            Spacer(modifier = Modifier.width(6.dp))
            ModeSelector2(label="I",
                icon = Icons.Default.AirplanemodeActive,
                color= COLOR_TRAVEL,
                selected=editMode == EditMode2.TRAVEL,
                onClick = {
                    editMode =
                        if (editMode == EditMode2.TRAVEL) EditMode2.NONE
                        else EditMode2.TRAVEL
                })
            Spacer(modifier = Modifier.width(6.dp))
            ModeSelector2(label="V",
                icon = Icons.Default.BeachAccess,
                color= COLOR_HOLIDAY,
                selected=editMode == EditMode2.HOLIDAY,
                onClick = {
                    editMode =
                        if (editMode == EditMode2.HOLIDAY) EditMode2.NONE
                        else EditMode2.HOLIDAY
                })
            Spacer(modifier = Modifier.width(6.dp))
            ModeSelector2(label="F",
                icon = Icons.Default.Bed,
                color= COLOR_FESTIVE,
                selected=editMode == EditMode2.FESTIVE,
                onClick = {
                    editMode =
                        if (editMode == EditMode2.FESTIVE) EditMode2.NONE
                        else EditMode2.FESTIVE
                })
        }

        // Build calendar matrix: list of weeks, each week is list of LocalDate?
        // (null for leading/trailing blanks)
        val weeks = remember(year, month) {
            buildMonthMatrix(year, month)
        }

        // Map dates to DayDto for quick lookup
        val dayMap = remember(monthDto.days) { monthDto.days.associateBy { it.date }}

        Column(
            modifier = Modifier.fillMaxWidth()) {
            // Weekday headers (Mon..Sun). Choose MOnday start; change if needed.
            Row(modifier = Modifier.fillMaxWidth()) {
                val headers = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                for (h in headers) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
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
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (date in week) {
                        Box(
                            modifier= Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                        ) {
                            if (date == null) {
                                // empty cell
                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = 5.dp,
                                        color = CELL_BORDER,
                                        shape = RoundedCornerShape(6.dp)
                                    ))
                            } else {
                                val dto = dayMap[date]
                                DayCellTap(
                                    date = date,
                                    dto = dto,
                                    editMode = editMode,
                                    onApply = { date, modality ->
                                        // apply mode if allowed
                                        if ((editMode != EditMode2.NONE)
                                            && !isNonEditableDay(dto)) {
                                                onApplyModality(date, modality)
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
private fun ToggleButton(label: String, colorBase: Color,  selected: Boolean, onClick: () -> Unit) {
    val dark = colorBase.changeSL(0.549f)
    Button(
        onClick= {
            onClick()
        } ,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) colorBase else dark,
            contentColor = Color.White
            //,
            //contentColor= if (selected)
        ),
        modifier = Modifier.size(width = 48.dp, height = 36.dp),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(3.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ModeSelector(label: String, icon: ImageVector,
                         color: Color,  selected: Boolean, onClick:()->Unit) {
    val bg=if(selected) color.copy(alpha =  0.18f) else Color.Transparent
    val contentColor = if (selected) color else Color.Black

    Row (
        modifier = Modifier
            .padding(end = 4.dp)
            .background(bg, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier =  Modifier.size(28.dp),
            shape = CircleShape,
            color = if (selected) color else Color.LightGray.copy(alpha = 0.2f)
        ) {
            Icon(icon, contentDescription = label,
                tint =  Color.White, modifier = Modifier.padding(6.dp))
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(text =  label, color =  contentColor)
    }
}

@Composable
private fun ModeSelector2(label: String, icon: ImageVector,
                         color: Color,  selected: Boolean, onClick:()->Unit) {

    val bgSelected = color.copy(alpha =  0.22f)
    val bgUnselected = Color.Transparent

    val circleSelected = color
    val circleUnselected = color.copy(alpha =  0.65f)

    val textSelected = color
    val textUnselected = COLOR_FESTIVE

    val borderSelected = Color.Transparent
    val borderUnselected = color.copy(alpha =  0.5f)

    val containerBg =
        if (selected) bgSelected
        else bgUnselected

    val circleColor =
        if (selected) circleSelected
        else circleUnselected

    val textColor =
        if (selected) textSelected
        else textUnselected

    val elevation =
        if (selected) 3.dp
        else 0.dp

    val borderColor =
        if (selected) borderSelected
        else borderUnselected

    Row (
        modifier = Modifier
            .padding(end = 8.dp)
            .background(containerBg, RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                onClick = onClick,
                role = Role.Button
            )
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier =  Modifier.size(28.dp),
            shape = CircleShape,
            color = circleColor,
            tonalElevation = elevation
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "$label mode",
                tint =  Color.White,
                modifier = Modifier.padding(6.dp))
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text=label,
            color=textColor,
            style =  MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * Day cell: draws colored background based on modality / weekend / festive
 */
@Composable
private fun DayCellTap(
    date: LocalDate,
    dto: DayDto?,
    editMode: EditMode2,
    onApply: (date: LocalDate, modality: Modality) -> Unit
) {
    // Determine color
    val bg = when {
        dto?.isFestive() == true -> COLOR_FESTIVE
        DayDto.isWeekend(date) -> COLOR_WEEKEND
        dto?.isHoliday() == true -> COLOR_HOLIDAY
        dto?.isTelework() == true -> COLOR_TELEWORK
        dto?.isPresential() == true -> COLOR_PRESENTIAL
        dto?.isTravel() == true -> COLOR_TRAVEL
        dto?.isHoliday() == true -> COLOR_HOLIDAY
        dto?.isFestive() == true -> COLOR_FESTIVE
        else -> COLOR_UNASSIGNED
    }

    // Outline if unassigned to be readable on white
    val borderColor = if (bg == COLOR_UNASSIGNED) CELL_BORDER else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .background(color = bg, shape = RoundedCornerShape(6.dp))
            .padding(6.dp)
            .clickable {
                if (editMode != EditMode2.NONE && !isNonEditableDay(dto)) {
                    val modality =
                        when (editMode) {
                            EditMode2.TELEWORK -> Modality.TELEWORK
                            EditMode2.PRESENTIAL -> Modality.PRESENTIAL
                            EditMode2.TRAVEL -> Modality.TRAVEL
                            EditMode2.HOLIDAY -> Modality.HOLIDAY
                            EditMode2.FESTIVE -> Modality.FESTIVE
                            else -> Modality.UNASSIGNED
                        }
                    onApply(date, modality)
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
//    return dto.festive || dto.weekend
    return DayDto.isWeekend(dto.date)
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
            cursor=cursor.plus(value = 1, unit = DateTimeUnit.DAY)
            //cursor = cursor.plus(1, DateTimeUnit.DAY)
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
fun CalendarMonthEditorPreview() {
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
        MainScreenCalendarMonthEditor(
            year = 2026,
            month = 1,
            monthDto = MonthStatsDto(year = 2026, month = 1, sampleDays),
            onApplyModality = { dateIso, modality -> },
            isPreviousNavigable = false,
            isNextNavigable = true,
            modifier = Modifier,
            onPrevious = {},
            onNext = {}
        )}
}

fun java.time.LocalDate.toKxLocalDate(): LocalDate = LocalDate(year, monthValue, dayOfMonth)


private fun Context.getColorFromAttr(@AttrRes attrColor: Int): Int {
    val typedArray = theme.obtainStyledAttributes(intArrayOf(attrColor))
    val textColor = typedArray.getColor(0,0)
    typedArray.recycle()
    return textColor
}

private fun Color.changeSL(saturationFactor:Float=1f, lightnessFactor:Float=1f): Color {
    val hsl = FloatArray(3)
    val originalARGB = this.toArgb()

    this.alpha
    this.red
    this.green
    this.blue

    ColorUtils.colorToHSL(originalARGB, hsl)
    hsl[1] = hsl[1] * saturationFactor.coerceIn(0f, 1f)
    hsl[2] = hsl[2] * lightnessFactor.coerceIn(0f, 1f)
    return Color(ColorUtils.HSLToColor(hsl))
}

fun Color.toHexCode(): String {
    return String.format("#%08X", this.toArgb())
}



package orot.apps.smartcounselor.presentation.app_style

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import orot.apps.smartcounselor.R

val Pretendard = FontFamily(
    Font(R.font.pretendard_thin, FontWeight.W100),
    Font(R.font.pretendard_extralight, FontWeight.W200),
    Font(R.font.pretendard_light, FontWeight.W300),
    Font(R.font.pretendard_regular, FontWeight.W400),
    Font(R.font.pretendard_medium, FontWeight.W500),
    Font(R.font.pretendard_semibold, FontWeight.W600),
    Font(R.font.pretendard_bold, FontWeight.W700),
    Font(R.font.pretendard_extrabold, FontWeight.W800),
    Font(R.font.pretendard_black, FontWeight.W900)
)

@OptIn(ExperimentalTextApi::class)
val PretendardTypography = Typography(

    h1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    h2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    h3 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    h4 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    subtitle1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.02).em,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    subtitle2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = (-0.02).em,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    caption = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (-0.02).em,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )
)

@OptIn(ExperimentalTextApi::class)
val Typography.Display1: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 56.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

@OptIn(ExperimentalTextApi::class)
val Typography.Display2: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 36.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

@OptIn(ExperimentalTextApi::class)
val Typography.Display3: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 28.sp,
        lineHeight = 40.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

@OptIn(ExperimentalTextApi::class)
val Typography.Subtitle3: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = (-0.02).em,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

@OptIn(ExperimentalTextApi::class)
val Typography.LabelBold: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 12.sp,
        lineHeight = 14.4.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

@OptIn(ExperimentalTextApi::class)
val Typography.LabelRegular: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 14.4.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

@OptIn(ExperimentalTextApi::class)
val Typography.LabelTag: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 10.sp,
        lineHeight = 10.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

@OptIn(ExperimentalTextApi::class)
val Typography.BodyL: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

@OptIn(ExperimentalTextApi::class)
val Typography.BodyM: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

@OptIn(ExperimentalTextApi::class)
val Typography.BodyS: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

@OptIn(ExperimentalTextApi::class)
val Typography.Disclaimer: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (-0.02).em,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )
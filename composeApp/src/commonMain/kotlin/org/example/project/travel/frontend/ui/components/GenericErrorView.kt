package org.example.project.travel.frontend.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*

/**
 * Reusable friendly error view for any network/API failure.
 *
 * Shows a Lottie "something went wrong" animation, a friendly message, and a "Try Again" button.
 * Never exposes raw error messages, stack traces, or exception details to the user.
 *
 * @param onRetry Lambda called when the user taps "Try Again".
 * @param modifier Optional Modifier for the container.
 */
@Composable
fun GenericErrorView(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("sww.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(300.dp)
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF176FF3),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 8.dp,
                hoveredElevation = 8.dp
            ),
            contentPadding = PaddingValues(horizontal = 36.dp, vertical = 12.dp),
            modifier = Modifier.height(48.dp)
        ) {
            Text(
                text = "TRY AGAIN",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                letterSpacing = 1.2.sp
            )
        }
    }
}

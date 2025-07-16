package io.github.warforged5.mashkmp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconSelectionSheet(
    onDismiss: () -> Unit,
    onIconSelected: (String) -> Unit
) {
    val icons = remember {
        listOf(
            "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "😊", "😇",
            "🙂", "🙃", "😉", "😌", "😍", "🥰","😚",
            "😋", "😝", "😜", "🤪", "🤨", "🧐", "🤓", "😎", "🥸",
            "🤩", "🥳", "😏", "😒", "😔", "😟", "😕", "🙁", "☹️",
            "😣", "😖", "😫", "😩", "🥺", "😢", "😭", "😤", "😠", "😡",
            "🤬", "🤯", "😳", "🥵", "🥶", "😱", "😨", "😰", "😥", "😓",
            "🤗", "🤔", "🫣", "🤭", "🫢", "🫡", "🤫", "🫠", "🤥", "😶",
            "🫥", "😐", "😑", "😬", "🙄", "😯", "😮", "😲",
            "🥱", "😴", "🤤", "😪", "😮‍💨", "😵", "😵‍💫", "🤐", "🥴", "🤢",
            "🤮", "🤧", "😷", "🤒", "🤕", "🤑", "🤠", "😈", "👿", "👹",
            "👺", "🤡", "💩", "👻", "💀", "☠️", "👽", "👾", "🤖", "🎃",
            "🦾", "🖕", "✍️", "🙏", "🫵", "🦿", "💄", "💋", "👄", "🫦", "🦷", "👅", "👂",
            "👃", "👣", "👁️", "👀", "🫀", "🫁", "🧠", "🗣️", "👤",
            "👥", "🫂", "👶", "👧", "🧒", "👦", "👩‍🦱",
            "🧑‍🦱", "👨‍🦰", "👩‍🦳", "🧑‍🦳",
            "👨‍🦳", "👩‍🦲", "🧑‍🦲", "👨‍🦲", "🧔‍♀️", "🧔", "🧔‍♂️", "👵", "🧓", "👴",
            "👲", "👳‍♀️", "👮‍♂️" ,"👷",
            "👷‍♂️", "💂‍♀️", "🕵️‍♂️", "👩‍⚕️", "🧑‍⚕️", "👨‍⚕️", "👨‍🌾","👨‍🍳", "👩‍🎓", "🧑‍🎓", "👨‍🎓", "👩‍🎤",
            "🧑‍🎤", "👨‍🎤", "👨‍🏫", "👩‍🏭", "🧑‍🏭", "👨‍🏭", "👩‍💻", "🧑‍💻",
            "👨‍💻", "👩‍💼", "👩‍🔧", "👩‍🔬", "🧑‍🔬", "👨‍🔬",
            "👩‍🎨", "🧑‍🎨", "👨‍🎨", "👩‍🚒", "👩‍✈️", "🧑‍✈️", "👨‍✈️", "👩‍🚀",
            "🧑‍🚀", "👨‍🚀", "👩‍⚖️", "🧑‍⚖️", "👰‍♀️", "🤵",
            "🤵‍♂️", "👸", "🥷", "🦸‍♂️", "🦹",
            "🎅", "🧙‍♂️", "🧝",
            "🧛‍♀️", "🧟‍♀️", "🧞‍♂️", "🧜‍♀️",
            "🧜", "🧜‍♂️", "🧚‍♀️", "🧚", "🧚‍♂️", "💅", "🤳", "💃", "🕺", "👯‍♀️", "🕴️", "🧎‍♀️", "🧎", "🧎‍♂️", "🏃‍♀️", "🧍‍♀️", "🧍",
            "🧍‍♂️","👫", "👩‍❤️‍👨", "👩‍❤️‍💋‍👩",
            "💏", "👨‍❤️‍💋‍👨", "👩‍❤️‍💋‍👨", "👪", "👨‍👩‍👦", "👨‍👩‍👧", "🪢", "🧶", "🧵", "🪡", "🧥", "🥼", "🦺", "👚",
            "👕", "👖", "🩲", "🩳", "👔", "👗", "👙", "🩱", "👘", "🥻",
            "🥿", "👠", "👡", "👢", "👞", "👟", "🥾", "🧦", "🧤", "🧣",
            "🎩", "🧢", "👒", "🎓", "⛑️", "🪖", "👑", "💍", "👝", "👛",
            "👜", "💼", "🎒", "🧳", "👓", "🕶️", "🥽", "🌂"
        )
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Select an Icon", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 64.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(icons) { icon ->
                    Surface(
                        modifier = Modifier
                            .size(64.dp)
                            .clickable { onIconSelected(icon) },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(icon, style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }
            }
        }
    }
}
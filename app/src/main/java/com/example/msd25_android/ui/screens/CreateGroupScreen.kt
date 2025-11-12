package com.example.msd25_android.ui.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBoxScope.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.data.group.Group
import com.example.msd25_android.logic.data.user.User
import com.example.msd25_android.logic.viewmodels.GroupViewModel
import com.example.msd25_android.logic.viewmodels.UserViewModel
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.reflect.Member

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    onDone: () -> Unit,
    onBack: () -> Unit = {},
    groupViewModel: GroupViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {



    var name by remember { mutableStateOf("") }
    val members = remember { mutableStateListOf<User>() }
    val cs = MaterialTheme.colorScheme
    var phone by remember {mutableStateOf("")}

    val canCreate = name.isNotBlank() && members.isNotEmpty()

    val userRepository = UserRepository((LocalContext.current.applicationContext as Application).dataStore)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            phone = userRepository.currentPhoneNumber.first()!!
            val res = userViewModel.getUserByPhone(phone!!)
            members.add(res.data!!)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Group") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 2.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    if (!canCreate) {
                        Text(
                            text = "Enter a group name and add at least one member",
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                groupViewModel.createGroupWithMembers(Group(name = name), members)
                            }
                            onDone()
                                  },
                        enabled = canCreate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primary,
                            contentColor = cs.onPrimary,
                            disabledContainerColor = cs.surfaceVariant,
                            disabledContentColor = cs.onSurfaceVariant
                        )
                    ) {
                        Text("Create Group")
                    }
                }
            }
        }
    ) { p ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(p)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Group name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            MemberInputDropdown(members = members)

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    reverseLayout = true
                ) {
                    items(members, key = { it.id }) { m ->
                        MemberCard(
                            name = m.name,
                            onRemove = { members.remove(m) },
                            removable = m.phoneNumber != phone
                        )
                    }
                }
            }


            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MemberCard(
    name: String,
    onRemove: () -> Unit,
    removable: Boolean = false
) {
    val cs = MaterialTheme.colorScheme

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = cs.surfaceVariant,
            contentColor = cs.onSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(cs.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.first().uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = cs.onPrimaryContainer
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSurface
                )
            }
            if (removable) {
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Remove member",
                        tint = cs.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberInputDropdown(
    members: MutableList<User>,
    userViewModel: UserViewModel = viewModel(),
) {

    var friends by remember { mutableStateOf<List<User>>(emptyList()) }
    var options by remember { mutableStateOf<List<User>>(emptyList()) }
    val userRepository = UserRepository((LocalContext.current.applicationContext as Application).dataStore)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val phone = userRepository.currentPhoneNumber.first()
            val res = userViewModel.getUserWithFriends(phone!!)
            if (res.success) {
                friends = res.data!!.friends
            }
        }
    }

    // Declaring a boolean value to store
    // the expanded state of the Text Field
    var mExpanded by remember { mutableStateOf(false) }
    // Create a string value to store the selected city
    var mSelectedText by remember { mutableStateOf("") }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val filterSearch = {
        options = friends.filter { friend ->
            friend.name.toLowerCase(Locale.current).startsWith(mSelectedText.toLowerCase(Locale.current)) }
            .filter { friend ->
                members.none { member -> member.id == friend.id }
            }
    }

    // call once at the beginning
    filterSearch()

    Column {

        ExposedDropdownMenuBox(
            expanded = mExpanded,
            onExpandedChange = {mExpanded = !mExpanded}
        ) {
            // Create an Outlined Text Field
            // with icon and not expanded
            OutlinedTextField(
                value = mSelectedText,
                onValueChange = {
                    mSelectedText = it
                    filterSearch()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        // This value is used to assign to
                        // the DropDown the same width
                        mTextFieldSize = coordinates.size.toSize()
                    }
                    .menuAnchor(MenuAnchorType.PrimaryEditable, true),
                label = {Text("Search friends")},
                trailingIcon = {
                    Icon(icon,"contentDescription",
                        Modifier.clickable {
                            Log.w("dropdown", "${friends.size}")
                            mExpanded = !mExpanded
                        })
                }
            )

            // Create a drop-down menu with list of cities,
            // when clicked, set the Text Field text as the city selected
            ExposedDropdownMenu(
                expanded = mExpanded && options.isNotEmpty(),
                onDismissRequest = { mExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
            ) {
                options.forEach { friend ->
                    AddFriendCard(friend, members, filterSearch)
                }
            }
        }
    }
}

@Composable
fun AddFriendCard(friend: User, members: MutableList<User>, addCallback: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(14.dp)
    ) {
        Column {
            Text(
                text = friend.phoneNumber,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = friend.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = {
                members.add(friend)
                addCallback()
                      },
        ) {
            Text(text = "Add")
        }
    }


}

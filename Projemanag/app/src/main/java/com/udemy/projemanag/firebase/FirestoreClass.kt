package com.udemy.projemanag.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.udemy.projemanag.activities.*
import com.udemy.projemanag.models.Board
import com.udemy.projemanag.models.User
import com.udemy.projemanag.utils.Constants

class FirestoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    /**
     * This function is used to store registered user info
     */
    fun registerUser(activity: SignUpActivity, userInfo: User){
        Log.d("User", getCurrentUserID())
        // Users Tableを作成する
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener {
                e ->
                Log.e(activity.javaClass.simpleName, "Error Writing Documents")
            }
    }

    /**
     * This function is used to create board
     */
    fun createBoard(activity: CreateBoardActivity, board: Board){
        // Users Tableを作成する
        mFirestore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(activity,
                    "Board created successfully", Toast.LENGTH_LONG).show()
                activity.boardCreatedSuccessfully()
            }.addOnFailureListener {
                    exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                    "Error Writing creating a board", exception)
            }
    }

    /**
     * This function is used to store registered user info
     */
    fun loadUserData(activity: Activity, readBoardsList: Boolean = false){
        // Users Tableを作成する
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {document ->
                // User Objectを取得する
                val loggedUser = document.toObject(User::class.java)!!
                when(activity) {
                    // SignInActivityの場合、
                    is SignInActivity -> {
                        activity.signInSuccess(loggedUser)
                    }
                    // MainActivityの場合、
                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedUser, readBoardsList)
                    }
                    // MyProfileActivityの場合、
                    is MyProfileActivity -> {
                        activity.setUserDataInUI(loggedUser)
                    }
                }
            }.addOnFailureListener { e ->
                when(activity) {
                    // SignInActivityの場合、
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                    // MainActivityの場合、
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("SignInUser", "Error Writing Documents")
            }
    }

    /**
     * This function is used to get the current user ID from firebase authentication
     */
    fun getCurrentUserID(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if(currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    /**
     * A function to update the user profile data into the database.
     */
    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFirestore.collection(Constants.USERS) // Collection Name
            .document(getCurrentUserID()) // Document ID
            .update(userHashMap) // A hashmap of fields which are to be updated.
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Data updated successfully!")

                // Notify the success result.
                when (activity) {
                    is MainActivity -> {
                        activity.tokenUpdateSuccess()
                    }
                    is MyProfileActivity -> {
                        activity.profileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MyProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board.",
                    e
                )
            }
    }
    // END

    fun getBoardsList(activity: MainActivity){
        mFirestore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserID())
            .get()
            .addOnSuccessListener {
                document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val boardList: ArrayList<Board> = ArrayList()
                for(i in document.documents){
                    val board = i.toObject(Board::class.java)!!
                    board.documentId = i.id
                    boardList.add(board)
                }

                activity.populateBoardListToUI(boardList)
            }.addOnFailureListener{e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating documents", e)
            }
    }

    /**
     * get board detail info
     */
    fun getBoardDetails(activity: TaskListActivity, documentId: String){
        mFirestore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener {
                    document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                val board = document.toObject(Board::class.java)!!
                board.documentId = document.id
                activity.boardDetails(board)
            }.addOnFailureListener{e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating documents", e)
            }
    }

    /**
     * A function to create a task list in the board detail.
     */
    fun addUpdateTaskList(activity: Activity, board: Board) {
        // update処理に必要なTaskListHashMapを作成する
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList
        // TaskListをFirestoreに更新する
        mFirestore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            // 更新する成功の場合、
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "TaskList updated successfully.")

                if (activity is TaskListActivity) {
                    activity.addUpdateTaskListSuccess()
                } else if (activity is CardDetailsActivity) {
                    activity.addUpdateTaskListSuccess()
                }
            }
            // 更新する失敗の場合、
            .addOnFailureListener { e ->
                if (activity is TaskListActivity) {
                    activity.hideProgressDialog()
                } else if (activity is TaskListActivity) {
                    activity.hideProgressDialog()
                }
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }

    // (Step 7: Change the function parameters as required and also pass the result based on activity instance.)
    // START
    /**
     * A function to get the list of user details which is assigned to the board.
     */
    fun getAssignedMembersListDetails(activity: Activity, assignedTo: ArrayList<String>) {

        mFirestore.collection(Constants.USERS) // Collection Name
            .whereIn(
                Constants.ID,
                assignedTo
            ) // Here the database field name and the id's of the members.
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())

                val usersList: ArrayList<User> = ArrayList()

                for (i in document.documents) {
                    // Convert all the document snapshot to the object using the data model class.
                    val user = i.toObject(User::class.java)!!
                    usersList.add(user)
                }

                if(activity is MembersActivity) {
                    activity.setupMembersList(usersList)
                }else if(activity is TaskListActivity) {
                    activity.boardMembersDetailList(usersList)
                }
            }
            .addOnFailureListener { e ->
                if(activity is MembersActivity) {
                    activity.hideProgressDialog()
                }else if(activity is TaskListActivity){
                    activity.hideProgressDialog()
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board.",
                    e
                )
            }
    }
    // END

    // TODO (Step 4: Create a function to get the user details from Firestore Database using the email address.)
    // START
    /**
     * A function to get the user details from Firestore Database using the email address.
     */
    fun getMemberDetails(activity: MembersActivity, email: String) {

        // Here we pass the collection name from which we wants the data.
        mFirestore.collection(Constants.USERS)
            // A where array query as we want the list of the board in which the user is assigned. So here you can pass the current user id.
            .whereEqualTo(Constants.EMAIL, email)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())

                if (document.documents.size > 0) {
                    val user = document.documents[0].toObject(User::class.java)!!
                    // Here call a function of base activity for transferring the result to it.
                    activity.memberDetails(user)
                } else {
                    activity.hideProgressDialog()
                    activity.showErrorSnackBar("No such member found.")
                }

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details",
                    e
                )
            }
    }
    // END

    // TODO (Step 8: Create a function to assign a updated members list to board.)
    // START
    /**
     * A function to assign a updated members list to board.
     */
    fun assignMemberToBoard(activity: MembersActivity, board: Board, user: User) {

        val assignedToHashMap = HashMap<String, Any>()
        assignedToHashMap[Constants.ASSIGNED_TO] = board.assignedTo

        mFirestore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(assignedToHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "TaskList updated successfully.")
                activity.memberAssignSuccess(user)
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }
    // END
}
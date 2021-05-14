package com.udemy.projemanag.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.udemy.projemanag.R
import com.udemy.projemanag.adapters.TaskListItemsAdapter
import com.udemy.projemanag.firebase.FirestoreClass
import com.udemy.projemanag.models.Board
import com.udemy.projemanag.models.Card
import com.udemy.projemanag.models.Task
import com.udemy.projemanag.models.User
import com.udemy.projemanag.utils.Constants
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : BaseActivity() {
    // A global variable for Board Details.
    private lateinit var mBoardDetails: Board
    // A global variable for board document id as mBoardDocumentId
    private lateinit var mBoardDocumentId: String
    // A global variable for Assigned Members List.
    lateinit var mAssignedMembersDetailList: ArrayList<User>

    /**
     * A companion object to declare the constants.
     */
    companion object {
        //A unique code for starting the activity for result
        const val MEMBERS_REQUEST_CODE: Int = 13

        //  (Step 5: Add a unique request code for starting the activity for result.)
       const val CARD_DETAILS_REQUEST_CODE: Int = 14
        // END
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        // (Step 6 : Make the document id global.)
        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            mBoardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this, mBoardDocumentId)
    }

    /**
     * setup action bar
     */
    private fun setupActionBar(){
        setSupportActionBar(toolbar_task_list_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)

            // setup back arrow
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)

            // set action bar title
            actionBar.title = mBoardDetails.name
        }
        toolbar_task_list_activity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * Firestoreから取得したBoardDetailsをUIに表示する
     */
    fun boardDetails(board: Board){
        mBoardDetails = board

        hideProgressDialog()

        // Call the function to setup action bar.
        setupActionBar()

        //  (Step 8: Get all the members detail list which are assigned to the board.)
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAssignedMembersListDetails(
            this@TaskListActivity,
            mBoardDetails.assignedTo
        )
        // END
    }

    /**
     * Function is used to call when task list update is successful
     */
    fun addUpdateTaskListSuccess(){
        // TaskList更新が成功の場合、ProgressDialogを閉じる
        hideProgressDialog()

        // Firestoreからデータを取得する前ProgressDialogを表示する
        showProgressDialog(resources.getString(R.string.please_wait))

        // BoardDetailsをFirestoreから取得する
        FirestoreClass().getBoardDetails(this, mBoardDetails.documentId)
    }

    /**
     * create task list
     */
    fun createTaskList(taskListName: String){
        // Taskオブジェクトを作成する
        val task = Task(taskListName, FirestoreClass().getCurrentUserID())

        // TaskオブジェクトをTaskListの[0]位置に追加する
        mBoardDetails.taskList.add(0, task)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        // TaskListを保存する前ProgressDialogを表示する
        showProgressDialog(resources.getString(R.string.please_wait))

        // TaskListをFirestoreに保存する
        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    /**
     * Function is used to call update a task list
     */
    fun updateTaskList(position: Int, listName: String, model: Task){
        // Taskを作成する
        val task = Task(listName, model.createdBy)

        mBoardDetails.taskList[position] = task
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size -1 )

        // TaskListを更新する前ProgressDialogを表示する
        showProgressDialog(resources.getString(R.string.please_wait))

        // TaskListをFirestoreに更新する
        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    /**
     * Function is used to call delete a task list
     */
    fun deleteTaskList(position: Int){
        mBoardDetails.taskList.removeAt(position)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size -1 )

        // TaskListを削除する前ProgressDialogを表示する
        showProgressDialog(resources.getString(R.string.please_wait))

        // TaskListをFirestoreに更新する
        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    // TODO (Step 5: A function to create a card and update it in the task list.)
    // START
    /**
     * A function to create a card and update it in the task list.
     */
    fun addCardToTaskList(position: Int, cardName: String) {
        // Remove the last item
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
        // ArrayList作成
        val cardAssignedUsersList: ArrayList<String> = ArrayList()
        cardAssignedUsersList.add(FirestoreClass().getCurrentUserID())
        // Cardオブジェクト作成
        val card = Card(cardName, FirestoreClass().getCurrentUserID(), cardAssignedUsersList)

        val cardsList = mBoardDetails.taskList[position].cards
        cardsList.add(card)

        val task = Task(
            mBoardDetails.taskList[position].title,
            mBoardDetails.taskList[position].createdBy,
            cardsList
        )

        mBoardDetails.taskList[position] = task

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@TaskListActivity, mBoardDetails)
    }
    // END

    // TODO (Step 7: Inflate the action menu for TaskListScreen and also launch the MembersActivity Screen on item selection.)
    // START
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu to use in the action bar
        menuInflater.inflate(R.menu.menu_members, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_members -> {
                //  (Step 2: Pass the board details through intent.)
                val intent = Intent(this@TaskListActivity, MembersActivity::class.java)
                intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
                //  (Step 2: Start activity for result.)
                startActivityForResult(intent, MEMBERS_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // END

    // (Step 8: Add the onActivityResult function add based on the requested document get the updated board details.)
    // START
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // (Step 7: Get the success result from Card Details Activity.)
        if (resultCode == Activity.RESULT_OK
            && (requestCode == MEMBERS_REQUEST_CODE || requestCode == CARD_DETAILS_REQUEST_CODE))
        {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getBoardDetails(this@TaskListActivity, mBoardDocumentId)
        }
        // END
        else {
            Log.e("Cancelled", "Cancelled")
        }
    }
    // END

    //  (Step 6: Create a function for viewing and updating card details.)
    /**
     * A function for viewing and updating card details.
     */
    fun cardDetails(taskListPosition: Int, cardPosition: Int) {
        val intent = Intent(this@TaskListActivity, CardDetailsActivity::class.java)
        intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
        intent.putExtra(Constants.TASK_LIST_ITEM_POSITION, taskListPosition)
        intent.putExtra(Constants.CARD_LIST_ITEM_POSITION, cardPosition)
        // (Step 9: Pass the Assigned members board details list to the card detail screen.)
        // START
        intent.putExtra(Constants.BOARD_MEMBERS_LIST, mAssignedMembersDetailList)
        startActivityForResult(intent, CARD_DETAILS_REQUEST_CODE)
        // END
    }

    /**
     * A function to get assigned members detail list.
     */
    fun boardMembersDetailList(list: ArrayList<User>) {

        mAssignedMembersDetailList = list

        hideProgressDialog()

        // Here we are appending an item view for adding a list task list for the board.
        val addTaskList = Task(resources.getString(R.string.add_list))
        mBoardDetails.taskList.add(addTaskList)

        rv_task_list.layoutManager =
            LinearLayoutManager(this@TaskListActivity,
                LinearLayoutManager.HORIZONTAL, false)
        rv_task_list.setHasFixedSize(true)

        // Create an instance of TaskListItemsAdapter and pass the task list to it.
        val adapter = TaskListItemsAdapter(this@TaskListActivity, mBoardDetails.taskList)
        rv_task_list.adapter = adapter // Attach the adapter to the recyclerView.

    }
    // END

    /**
     * A function to update the card list in the particular task list.
     */
    fun updateCardsInTaskList(taskListPosition: Int, cards: ArrayList<Card>) {

        // Remove the last item
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        mBoardDetails.taskList[taskListPosition].cards = cards

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@TaskListActivity, mBoardDetails)
    }
    // END
}
package gfc.frontend

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.controllers.FamilyController
import gfc.frontend.controllers.RewardsController
import gfc.frontend.controllers.TasksController
import gfc.frontend.databinding.ActivityNewTaskBinding
import gfc.frontend.dataclasses.Reward
import gfc.frontend.dataclasses.Task
import gfc.frontend.requests.RewardDTO
import gfc.frontend.requests.TaskDTO
import gfc.frontend.service.KtorService
import gfc.frontend.service.TasksService
import java.util.regex.Matcher
import java.util.regex.Pattern

class NewTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        binding = ActivityNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        val rewards = intent.getBooleanExtra("rewards", false)
        val edit = intent.getBooleanExtra("edit", false)

        RewardsController.init(applicationContext)
        TasksController.init(applicationContext)
        FamilyController.init(this)

        var taskID = 0L
        var rewardID = 0L

        if (edit) {
            if (rewards) {
                actionBar!!.title = "Edytuj nagrodę"
                rewardID = intent.getLongExtra("rewardId", 0)
                binding.repeteable.isVisible = false
            } else {
                actionBar!!.title = "Edytuj zadanie"
                taskID = intent.getLongExtra("taskId", 0)
            }
            binding.name.setText(intent.getStringExtra("name"))
            binding.describtion.setText(intent.getStringExtra("description"))
            binding.points.setText(intent.getStringExtra("points"))
            binding.repeteable.isChecked = intent.getBooleanExtra("repeatable", false)
            val list = ArrayList<String>()
            intent.getStringExtra("selectedChild")?.let { list.add("$it (${FamilyController.getChildrenUsername(it)})") }
            val childrenChoice = binding.childrenChoice
            childrenChoice.isVisible = true
            childrenChoice.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            childrenChoice.isEnabled = false
            binding.deleteButton.isVisible = true
        } else {
            if(rewards) {
                actionBar!!.title = "Dodaj nową nagrodę"
                binding.repeteable.isVisible = false
            }
            else {
                actionBar!!.title = "Dodaj nowe zadanie"
            }

            if(AuthorizationController.userIsParent){
                val list = ArrayList<String>()
                FamilyController.familyList.forEach { e -> list.add("${e.friendlyName} (${e.username})") }
                binding.childrenChoice.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
                binding.childrenChoice.isVisible = true
            }
        }

        actionBar.setDisplayHomeAsUpEnabled(false)

        binding.acceptButton.setOnClickListener { view ->
            val ownerId = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getLong("id", 0)
            var name = binding.name.text.toString()
            val description = binding.describtion.text.toString()
            val points = binding.points.text.toString()
            val repeatable = binding.repeteable.isChecked
            var ret: Long? = null

            if (name == "") {
                name = if(rewards) {
                    "Nagroda bez nazwy"
                } else {
                    "Zadanie bez nazwy"
                }
            }

            var pointsL = 0L

            if (points != "") {
                pointsL = points.toLong()
            }

            if(AuthorizationController.userIsParent){
                val label = binding.childrenChoice.selectedItem.toString().trim()
                var response = ""
                var childId = -1L

                if(!label.startsWith("prywatne")) {
                    for(s in label.reversed()) {
                        if(s == ')'){
                            continue
                        }
                        if(s != '('){
                            response += s
                            continue
                        }
                        if (s == '(') {
                            childId = FamilyController.getChildrenId(response.reversed())
                            if(childId != -1L) {
                                break
                            }
                        }
                    }
                } else {
                    childId = ownerId
                }
                if(edit) {
                    if(rewards) {
                        val reporter = FamilyController.getMemberById(ownerId)
                        val owner = FamilyController.getMemberById(childId)
                        if(reporter == null || owner == null) {
                            println("ERR: Error: reporter: $reporter and owner: $owner")
                            return@setOnClickListener
                        }
                        ret = RewardsController.editReward (
                            Reward(rewardID, name, description, reporter, owner, false, pointsL)
                        )
                    }
                    else {
                        ret = TasksController.editTask (
                            Task(taskID, childId, name, description, pointsL, false),
                            repeatable
                        )
                    }
                } else {
                    if(rewards) {
                        val reward = if(childId == ownerId) {
                            RewardDTO(name, description, childId, childId, false, pointsL)
                        } else {
                            RewardDTO(name, description, ownerId, childId, false, pointsL)
                        }
                        ret = RewardsController.addReward( reward )
                    }
                    else {
                        ret = if(childId == ownerId){
                            TasksService.addTask(
                                "https://gamefication-for-children.herokuapp.com/tasks/add",
                                TaskDTO(childId, name, description, pointsL, repeatable, true)
                            )
                        } else {
                            TasksService.addTask(
                                "https://gamefication-for-children.herokuapp.com/tasks/add",
                                TaskDTO(childId, name, description, pointsL, repeatable, false)
                            )
                        }
                    }
                }
            } else {
                if(edit) {
                    ret = if(rewards) {
                        val reporter = FamilyController.getFamilyParent()
                        val owner = FamilyController.getMemberById(ownerId)
                        if(reporter == null || owner == null) {
                            println("ERR: Error: reporter: $reporter and owner: $owner")
                            return@setOnClickListener
                        }
                        RewardsController.editReward (
                            Reward(rewardID, name, description, reporter, owner, false, pointsL)
                        )
                    } else{
                        TasksController.editTask(
                            Task(taskID, ownerId, name, description, pointsL, true),
                            repeatable
                        )
                    }
                } else {
                    ret = if(rewards) {
                        val reward = RewardDTO(name, description, ownerId, ownerId, false, pointsL)
                        RewardsController.addReward( reward )
                    } else{
                        TasksService.addTask(
                            "https://gamefication-for-children.herokuapp.com/tasks/add",
                            TaskDTO(ownerId, name, description, pointsL, repeatable, true)
                        )
                    }
                }
            }

            if (ret != null) {
                Snackbar.make(view, "Zapisano", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

                setResult(RESULT_OK, Intent())
                finish()
            }
            else {
                Snackbar.make(view, "Błąd serwera! Zadanie nie zostało dodane.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }

        binding.deleteButton.setOnClickListener { view ->
            if (rewards) {
                val ret = RewardsController.deleteReward( rewardID )
                if (ret == null || ret == -1L) {
                    Snackbar.make(view, "Błąd serwera! Nagroda nie została usunięta.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                } else {
                    setResult(RESULT_OK, Intent())
                    finish()
                }
            } else {
                val ret = TasksController.deleteTask(taskID)
                if (ret == null || ret == -1L) {
                    Snackbar.make(view, "Błąd serwera! Zadanie nie zostało usunięte.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                } else {
                    setResult(RESULT_OK, Intent())
                    finish()
                }
            }
        }

        binding.cancelButton.setOnClickListener { view ->
            finish()
        }
    }
}
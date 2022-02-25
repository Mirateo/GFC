package gfc.frontend

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.controllers.FamilyController
import gfc.frontend.controllers.TasksController
import gfc.frontend.databinding.ActivityNewTaskBinding
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
        TasksController.init(applicationContext)

        binding = ActivityNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar


        if (intent.getBooleanExtra("edit", false)) {
            actionBar!!.title = "Edytuj zadanie"

            binding.name.setText(intent.getStringExtra("name"))
            binding.describtion.setText(intent.getStringExtra("description"))
            binding.points.setText(intent.getStringExtra("points"))
            binding.repeteable.isChecked = intent.getBooleanExtra("repeatable", false)
            val list = ArrayList<String>()
            intent.getStringExtra("selectedChild")?.let { list.add(it) }
            binding.childrenChoice.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
            binding.childrenChoice.isVisible = true
        }
        else {
            actionBar!!.title = "Dodaj nowe zadanie"

            if(AuthorizationController.userIsParent){
                FamilyController.init(this)
                val list = ArrayList<String>()
                FamilyController.familyList.forEach { e -> list.add("${e.friendlyName} (${e.username})") }
                binding.childrenChoice.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
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
            var ret: Long?

            if (name == "") {
                name = "Zadanie bez nazwy"
            }

            var pointsL = 0L

            if (points != "") {
                pointsL = points.toLong()
            }

            if(AuthorizationController.userIsParent){
                val label = binding.childrenChoice.selectedItem.toString()
                var response = ""
                var childId = -1L

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

            } else {
                ret = TasksService.addTask(
                    "https://gamefication-for-children.herokuapp.com/tasks/add",
                    TaskDTO(ownerId, name, description, pointsL, repeatable, true)
                )
            }

            if (ret != null) {
                Snackbar.make(view, "Nowe zadanie dodane", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            else {
                Snackbar.make(view, "Błąd serwera! Zadanie nie zostało dodane.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            setResult(RESULT_OK, Intent())
            finish()
        }

        binding.cancelButton.setOnClickListener { view ->
            finish()
        }
    }
}
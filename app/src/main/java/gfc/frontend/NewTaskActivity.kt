package gfc.frontend

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.controllers.TasksController
import gfc.frontend.databinding.ActivityNewTaskBinding
import gfc.frontend.requests.TaskDTO
import gfc.frontend.service.KtorService
import gfc.frontend.service.TasksService

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
        }
        else {
            actionBar!!.title = "Dodaj nowe zadanie"
        }

        actionBar.setDisplayHomeAsUpEnabled(false)

        

        binding.acceptButton.setOnClickListener { view ->
            TasksService.addTask(
                "https://gamefication-for-children.herokuapp.com/tasks/add",
                TaskDTO(
                    ownerId = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getLong("id", 0),
                    name = binding.name.text.toString(),
                    description = binding.describtion.text.toString(),
                    points = binding.points.text.toString().toLong(),
                    repeatable = binding.repeteable.isChecked
                )
            )
            Snackbar.make(view, "Nowe zadanie dodane", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            setResult(RESULT_OK, Intent())
            finish()
        }

        binding.cancelButton.setOnClickListener { view ->
            finish()
        }
    }
}
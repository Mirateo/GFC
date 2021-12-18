package gfc.frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import gfc.frontend.controllers.TasksController
import gfc.frontend.databinding.ActivityNewTaskBinding
import gfc.frontend.dataclasses.TaskDTO

class NewTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        binding = ActivityNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.title = "Dodaj nowe zadanie"
        }

        actionBar!!.title = "Dodaj nowe zadanie"
        actionBar.setDisplayHomeAsUpEnabled(false)

        binding.acceptButton.setOnClickListener { view ->
            val tmp = TasksController(this)
            tmp.addTask(binding.name.text.toString(), binding.describtion.text.toString(),
                binding.points.text.toString().toLong(), binding.repeteable.isActivated)
            Snackbar.make(view, "Now zadanie dodane", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        binding.cancelButton.setOnClickListener { view ->
            finish()
        }
    }
}
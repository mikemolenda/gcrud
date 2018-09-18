package com.cna.gcrud

import com.google.cloud.datastore.*
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/task")
class TaskController {

    private val datastore = DatastoreOptions.getDefaultInstance().service

    @RequestMapping(
            value = ["/create"],
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createEntity(@RequestBody(required = true) task: Task): Response {
        return try {
            val keyFactory = datastore.newKeyFactory().setKind("Task")
//            val taskKey = datastore.allocateId(keyFactory.newKey())
//            val taskEntity = Entity
//                    .newBuilder(taskKey)
//                    .set("category", task.category)
//                    .set("priority", task.priority)
//                    .set("description", task.description)
//                    .set("done", task.done)
//                    .build()
//            datastore.put(taskEntity)
            val taskEntity = Entity
                    .newBuilder(keyFactory.newKey("static"))
                    .set("category", task.category)
                    .set("priority", task.priority)
                    .set("description", task.description)
                    .set("done", task.done)
                    .build()
            datastore.put(taskEntity)
            Response(task)
        } catch (e: Exception) {
            Response(e)
        }
    }

    @RequestMapping(
            value = ["/retrieve"],
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun retrieveEntity(@RequestParam(name = "id", required = true) id: Long): Response {
        return try {
            val taskKey = datastore.newKeyFactory().newKey(id)
            val taskEntity = datastore.get(taskKey)
            val task = Task(
                    category = taskEntity.getString("category"),
                    priority = taskEntity.getLong("priority"),
                    description = taskEntity.getString("description"),
                    done = taskEntity.getBoolean("done")
            )
            Response(task)
        } catch (e: Exception) {
            Response(e)
        }
    }

    @RequestMapping(
            value = ["/retrieveAll"],
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun retrieveAllEntities(): Response {
        return try {
            val query = Query.newEntityQueryBuilder()
                    .setKind("Task")
                    .build()
            val tasks = mutableListOf<Task>()
            datastore.run(query).forEach {
                tasks.add(Task(
                        category = it.getString("category"),
                        priority = it.getLong("priority"),
                        description = it.getString("description"),
                        done = it.getBoolean("done")
                ))
            }
            Response(tasks)
        } catch (e: Exception) {
            Response(e)
        }
    }

}

data class Task(
        val category: String,
        var priority: Long,
        var description: String,
        var done: Boolean = false
)

data class Response(
        val body: Any
)
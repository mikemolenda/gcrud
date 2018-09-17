package com.cna.gcrud

import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Entity
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class GcrudController {

    private val datastore = DatastoreOptions.getDefaultInstance().service

    @RequestMapping(value = ["/create"], method = [RequestMethod.POST], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createEntity(@RequestBody(required = true) task: Task): Response {
        try {
            val keyFactory = datastore.newKeyFactory().setKind("Task")
            val taskKey = datastore.allocateId(keyFactory.newKey())
            val taskEntity = Entity
                    .newBuilder(taskKey)
                    .set("category", task.category)
                    .set("priority", task.priority)
                    .set("description", task.description)
                    .set("done", task.done)
                    .build()
            datastore.put(taskEntity)
        } catch (e: Exception) {
            return Response("Create task error", e)
        }
        return Response("Create task success", task)
    }

}

data class Task(
        val category: String,
        var priority: Long,
        var description: String,
        var done: Boolean = false
)

data class Response(
        val message: String,
        val payload: Any
)
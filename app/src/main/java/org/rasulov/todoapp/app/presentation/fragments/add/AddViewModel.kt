package org.rasulov.todoapp.app.presentation.fragments.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.rasulov.todoapp.app.data.todo.ToDoRepository
import org.rasulov.todoapp.app.domain.entities.ToDo
import org.rasulov.todoapp.app.data.EmptyFieldException
import org.rasulov.todoapp.app.presentation.utils.EmptyFieldUIEvent
import org.rasulov.androidx.lifecycle.MutableSingleUseData
import org.rasulov.todoapp.app.presentation.utils.UIEvent
import org.rasulov.androidx.lifecycle.toSingleUseData
import org.rasulov.todoapp.app.domain.Repository
import org.rasulov.todoapp.app.presentation.utils.OperationSuccessUIEvent

class AddViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiEvent = MutableSingleUseData<UIEvent>()
    val uiEvent = _uiEvent.toSingleUseData()

    fun addToDo(toDo: ToDo) = viewModelScope.launch {
        try {
            repository.addToDo(toDo)
            _uiEvent.value = OperationSuccessUIEvent
        } catch (e: EmptyFieldException) {
            _uiEvent.value = EmptyFieldUIEvent
        }

    }
}
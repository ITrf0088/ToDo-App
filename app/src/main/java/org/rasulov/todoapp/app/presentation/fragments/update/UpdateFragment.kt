package org.rasulov.todoapp.app.presentation.fragments.update

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.rasulov.androidx.fragment.addMenuProvider
import org.rasulov.androidx.fragment.getGradientDrawable
import org.rasulov.androidx.fragment.viewBindings
import org.rasulov.todoapp.R
import org.rasulov.todoapp.app.Singletons
import org.rasulov.todoapp.app.domain.entities.Priority
import org.rasulov.todoapp.app.domain.entities.ToDo
import org.rasulov.todoapp.app.presentation.utils.*
import org.rasulov.todoapp.databinding.FragmentAddBinding


class UpdateFragment : Fragment(R.layout.fragment_update) {

    private val viewModel by viewModel {
        UpdateViewModel(Singletons.toDoRepository)
    }

    private val binding: FragmentAddBinding by viewBindings()

    private val controller by lazy { findNavController() }

    private val args by navArgs<UpdateFragmentArgs>()

    private val colors by lazy {
        requireContext().getColorsFromRes(R.array.colors)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()

        addMenuProvider()

        setSpinnerOnItemSelected()

        observeUiEvent()

    }

    private fun setUpViews() {
        binding.toDoViews.apply {
            edtTitle.setText(args.item.title)
            spinnerPriority.setSelection(args.item.priority.ordinal - 1)
            edtDescription.setText(args.item.description)
        }
    }

    private fun addMenuProvider() = addMenuProvider(
        menuRes = R.menu.update_fragment,
        onMenuItemSelected = {
            when (it.itemId) {
                R.id.menu_save -> viewModel.updateToDO(getToDo())
                R.id.menu_delete -> viewModel.deleteToDO(args.item.id)
                android.R.id.home -> controller.popBackStack()
            }
        }
    )


    private fun getToDo(): ToDo {
        binding.toDoViews.apply {
            val title = edtTitle.text.toString()
            val priority = Priority.values()[spinnerPriority.selectedItemPosition + 1]
            val description = edtDescription.text.toString()
            return ToDo(args.item.id, title, priority, description)
        }
    }

    private fun setSpinnerOnItemSelected() {
        binding.toDoViews.spinnerPriority.setOnItemSelectedListener { item, position ->
            val drawable = getGradientDrawable(R.drawable.spinner_shape)
            drawable.setStroke((2).dpToPixel(requireContext()), colors[position])
            (item as? TextView)?.setTextColor(colors[position])
            binding.toDoViews.spinnerPriority.background = drawable
        }
    }

    private fun observeUiEvent() = viewModel.uiEvent.observe(viewLifecycleOwner) {
        when (it) {
            EmptyFieldUIEvent -> showToast()
            OperationSuccessUIEvent -> controller.popBackStack()
        }
    }

    private fun showToast() {
        Toast.makeText(
            requireContext().applicationContext,
            "The Title field is empty",
            Toast.LENGTH_LONG
        ).show()
    }
}
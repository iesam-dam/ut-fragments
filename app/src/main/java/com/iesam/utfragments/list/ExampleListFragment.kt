package com.iesam.utfragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.iesam.utfragments.MainActivity
import com.iesam.utfragments.R
import com.iesam.utfragments.databinding.FragmentExampleListBinding
import com.iesam.utfragments.detail.ExampleDetailFragment

class ExampleListFragment : Fragment() {

    private var _binding: FragmentExampleListBinding? = null

    //Esta es la forma de sobreescribir el getter por defecto de un atributo
    private val binding get() = _binding!!

    val viewModel by viewModels<ExampleListViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExampleListBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.apply {
            actionChangeFragment.setOnClickListener {
                (activity as MainActivity).changeFragment(ExampleDetailFragment.newInstance("1"))
            }
            layoutList.toolbar.apply {
                //Establecemos el título por programación
                title = getString(R.string.fragment_list_title)

                //Inflamos|Añadimos el menú por programación.
                inflateMenu(R.menu.menu_list)

                //Añadimos los eventos a los botones del menú
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_favorite -> {
                            //Ejecutamos una acción
                            showFavoriteItems()
                            //Siempre hay que devolver true para que quede pulsado
                            true
                        }

                        R.id.action_delete -> {
                            deleteItems()
                            //Siempre hay que devolver true para que quede pulsado
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }

    private fun hideMenuItem() {
        val itemFavorite = binding.layoutList.toolbar.menu.findItem(R.id.action_favorite)
        itemFavorite.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_fill)
    }

    private fun showFavoriteItems() {
        // Hacer algo
    }

    private fun deleteItems() {
        // Hacer algo
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        val observer = Observer<ExampleListViewModel.UiState> {

        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    private fun executeFunctionInActivity() {
        (activity as MainActivity).showMessage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ExampleListFragment()
    }
}
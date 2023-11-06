package com.iesam.utfragments.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.iesam.utfragments.MainActivity
import com.iesam.utfragments.R
import com.iesam.utfragments.databinding.FragmentExampleDetailBinding
import com.iesam.utfragments.list.ExampleListFragment

class ExampleDetailFragment : Fragment() {

    private var _binding: FragmentExampleDetailBinding? = null
    private val binding get() = _binding!!
    val viewModel by viewModels<ExampleDetailViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExampleDetailBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.apply {
            actionChangeFragment.setOnClickListener {
                (activity as MainActivity).changeFragment(ExampleListFragment.newInstance())
            }
            layoutDetail.toolbar.apply {
                //Establecemos el título por programación
                title = getString(R.string.fragment_detail_title)

                //Inflamos|Añadimos el menú por programación.
                inflateMenu(R.menu.menu_detail)

                //Añadimos el icono por programación
                setNavigationIcon(R.drawable.ic_close)

                //Añadimos una acción al botón de navegación
                setNavigationOnClickListener {
                    //Volver a atrás...
                }

                //Añadimos los eventos a los botones del menú
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_add -> {
                            //Mostramos un mensaje
                            addItem()
                            //Siempre hay que devolver true para que quede pulsado
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }

    private fun addItem() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        val observer = Observer<ExampleDetailViewModel.UiState> {

        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ExampleDetailFragment()
    }
}
# Fragments (fragmentos)

Un **Fragmento** representa una parte reutilizable de la interfaz de usuario de su aplicación.
Un fragmento define y gestiona su propio diseño, tiene su propio ciclo de vida y puede gestionar sus
propios eventos de entrada.
Los fragmentos no pueden vivir por sí solos. **Deben estar alojados en una actividad u otro
fragmento.**

## Reutilización (modularidad)

Los fragmentos introducen modularidad y reutilización en la interfaz de usuario de su actividad al
permitirle dividir la interfaz de usuario en fragmentos.

Aquí se puede ver un ejemplo de una aplicación con dos fragmentos. Si la aplicación se ejecuta en
una tablet se pueden alinear los fragmentos de forma horizontal, si es un móvil, se pueden alinear
de forma vertical.

!(/assets/fragment-screen-sizes.png)

## Crear un Fragment

No hace falta añadir ninguna dependencia para tener acceso a los fragmentos del paquete androidX
pero si es recomendable añadir funciones extras sobre fragmentos a través de la siguiente
librería:

```
    val fragment_version = "1.6.2"
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
```

Las clases de tipo Fragments que se creen deben extender de **Fragments()**.

```
    class ExampleFragment : Fragment(){
    
    }
```

En el layout del Activity, hay que añadir un **FragmentContainerView** e indicar:

- Un **id**.
- En el atributo **name** la clase del fragment a cargar. Una vez se inicie la actividad, se creará
  el fragmento.

Estas clases **no son registradas** en el fichero AndroidManifest.xml.

Para cargar cualquier otro fragmento en el FragmentContainerView debemos:

```
    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
```

Donde el ``R.id.fragment_container`` el el FragmentContainerView y el ``fragment``es una instancia
del fragmento que queremos cargar. Este fragmento reemplazará al fragmento anterior.

## Ciclo de Vida de un Fragment

Los fragmentos tienen un ciclo de vida propio que depende del ciclo de vida contenedor (Activity u
otro fragment).

En la actualidad, las aplicaciones se desarrollan siguiente la arquitectura **Single Activity**, es
decir, la aplicación tendrá sólo un Activity el cual servirá de contenedor para todos los Fragments
que se usen en la app.

Cada pantalla que se desarrolle será un Fragment. **El Fragment lleva asociado: un ViewModel.**

**Es muy importante** tener claro cada una de las llamadas que tiene un Fragment para saber dónde se
tienen que inicializar cada uno de las funcionalidades: ViewModel, Observers, etc.

![](/assets/fragment-view-lifecycle.png)

### 1. Iniciar Hilt

Para usar Hilt en un fragment hay que añadir la anotación:

```
@AndroidEntryPoint
class OffersListFragment : Fragment() {
    ...
}
```

### 2. Iniciar ViewBinding

Es importante tener en cuenta cuando hay que crear el viewBinding y cómo hay que liberarlo
estableciendolo a null.

```
    private var _binding: ResultProfileBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExampleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
```

### 3. Configurar la vista (setupView)

Si queremos personalizar la vista añadiendo eventos sobre algunos de los elementos de la interfaz de
usuario, debemos hacerlo en el método del ciclo de vida adecuado.

**Recuerda:** En el setupView no debería actualizarse nada que dependa del ViewModel

```
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EjemploBinding.inflate(inflater, container, false)
        setupView() //aquí añadir eventos, títulos, etc. 
        return binding.root
    }
```

### 4. Crear el observer para comunicar el ViewModel y el Fragment

El observer se usa y se crea igual que se hace en un Activity. Lo único que cambia es el ciclo de
vida que se usa en los Fragments:

```
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }
    
    private fun setupObservers(){
        val observer = Observer<ExampleViewModel.UiState>{
          ...
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }
```

### 5. Personalizar la Toolbar

La toolbar se añade en la vista que usa el fragmento así podremos tener una toolbar personalizada
por cada fragmento. Al usar el mismo layout de la toolbar en varios fragmentos, debemos
personalizarla por programación.

Ejemplo: Vamos a establecer el título, un menú y las acciones por cada uno de los items que tiene el
menú.

```
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
```

Si queremos manipular el menú una vez cargado, por ejemplo, para mostrar diferentes estados de un
icono que acabamos de pulsar (favorito/no favorito) podemos hacerlo de la siguiente forma:

```
    private fun hideMenuItem() {
        val itemFavorite = binding.layoutList.toolbar.menu.findItem(R.id.action_favorite)
        itemFavorite.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_fill)
    }
```

### 6. Acceder a la Activity

Si queremos acceder al Actitity que ha cargado el fragment, se haría de la siguiente forma:

```
    //Tendríamos que hacer un casting, con el riesgo que conlleva.
    private fun executeFunctionInActivity() {
        (activity as MainActivity).showMessage()
    }
```

Otro ejemplo es:

```
    actionChangeFragment.setOnClickListener {
        (activity as MainActivity).changeFragment(ExampleDetailFragment.newInstance())
    }
```

## Bibliografía

- [Página Web Oficial Android](https://developer.android.com/guide/fragments)
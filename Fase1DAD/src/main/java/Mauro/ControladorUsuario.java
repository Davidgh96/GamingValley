package Mauro;

import javax.annotation.PostConstruct; 
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class ControladorUsuario {
	
	private Usuario usuarioLogeado;
	private Pedido pedidoActual;
	@Autowired
	private VideojuegoRepository repositorioVideojuegos;
	@Autowired
	private UsuarioRepository repositorioUsuarios;
	@Autowired
	private ValoracionRepository repositorioValoraciones;
	@Autowired
	private PedidoRepository repositorioPedidos;
	
	@PostConstruct
	public void init(){
		this.pedidoActual = new Pedido();
	}
	
	@GetMapping("/login_usuario")
	public String inicioSesion(Model model, HttpSession sesion){
		if(this.usuarioLogeado== null){
			return "login_usuario";
		}else{
			model.addAttribute("usuario",this.usuarioLogeado);
			return"login_correcto";
		}
	}
	
	@PostMapping("/logearse")
	public String logearse(Model model, HttpSession sesion, Usuario usuario){
		Usuario usuarioIntento = this.repositorioUsuarios.findByEmailAndPassword(usuario.getEmail(),usuario.getPassword());
		if(usuarioIntento==null){
			return "login_incorrecto";
		}else{
			this.usuarioLogeado = usuarioIntento;
			return "login_correcto";	
		}
	}
	
	@GetMapping("/registro")
	public String registroUsuario(Model model,Usuario usuario, HttpSession sesion){
		usuarioLogeado=usuario;
		repositorioUsuarios.save(usuarioLogeado);
		return "registro_correcto.html";
	}
	
	
	@RequestMapping("/pedidos")
	public String tablon(Model model,HttpSession sesion) {
		model.addAttribute("pedidos", repositorioPedidos.findByComprador(this.usuarioLogeado));
		return "pedidos";
	}
	//FALTA IMPLEMENTAR ESTE METODO
	@GetMapping("/pedido/{idPedido}/eliminar_videojuego_pedido/{idVideojuego}")
	public String eliminarVideojuegoPedido(HttpSession sesion, @PathVariable long idPedido,@PathVariable long idVideojuego){
		Pedido pedido = this.repositorioPedidos.findById(idPedido);
		Videojuego videojuego = this.repositorioVideojuegos.findById(idVideojuego);
		pedido.eliminarVideojuego(videojuego);
		pedido.costeTotalPedido();
		this.repositorioPedidos.save(pedido);
		return"videojuego_eliminado_pedido";
	}
	
	
	@GetMapping("/deslogearse")
	public String deslogearse(Model model,HttpSession sesion){
		if (this.usuarioLogeado != null){
			this.usuarioLogeado= null;
			this.pedidoActual=null;
			return "deslogeo_correcto";
		}else{
			return "no_logeado";
		}
		
	}


	@GetMapping("/form_registro")
	public String mostrarForm(Model model){
		return "registro_usuario.html";
	}

	
	//Método de prueba para ver si se registran los usuarios
	@RequestMapping("/usuario/lista_usuarios")
	public String verUsuariosLogeados(Model model){
		model.addAttribute("usuarios",repositorioUsuarios.findAll());
		return "usuarios";
	}
	
	@GetMapping("/nuevo_pedido_actual")
	public String pedidoActualNuevo(HttpSession sesion) {
		if(this.usuarioLogeado!=null){
			Pedido pedido = new Pedido();
			pedido.setComprador(this.usuarioLogeado);
			pedido.setFecha("17-02-2017");
			this.pedidoActual = pedido;
			this.repositorioPedidos.save(pedido);
			System.out.println(this.pedidoActual.getId());
			System.out.println(this.pedidoActual.getCesta());
			return"pedido_guardado";
		}else{
			return"logearse_necesita";
		}
	}
	
	@GetMapping("/cambiar_pedido_actual/{id}")
	public String cambiarPedidoActual(Model model,@PathVariable long id,HttpSession sesion){
		this.pedidoActual = this.repositorioPedidos.findByIdAndComprador(id, this.usuarioLogeado);
		model.addAttribute("id",id);
		System.out.println(this.pedidoActual.getId());
		System.out.println(this.pedidoActual.getCesta());
		return"pedido_actual_cambiado";
	}
	
	@GetMapping("/eliminar_pedido/{id}")
	public String eliminarPedido(@PathVariable long id,HttpSession sesion){
		Pedido pedido = this.repositorioPedidos.findByIdAndComprador(id,this.usuarioLogeado);
		this.repositorioPedidos.delete(pedido);
		return"pedido_eliminado";
	}
	
	@PostMapping("/agregar_videojuego_pedido_actual/{id}")
	public String agregar_v_pactual(@PathVariable long id,HttpSession sesion){
		Videojuego videojuego = this.repositorioVideojuegos.getOne(id);
		this.pedidoActual.agregarVideojuego(videojuego);
		this.pedidoActual.costeTotalPedido();
		this.repositorioPedidos.save(pedidoActual);
		System.out.println(pedidoActual.getCesta());
		return"videojuego_agregado_pedido";
	}

	
	@PostMapping("/videojuego/{id}/nueva_form_valoracion")
	public String agregarValoracion(Model model,@PathVariable long id, Valoracion valoracion,HttpSession sesion){
		model.addAttribute("id",id);
		Videojuego videojuego = this.repositorioVideojuegos.findOne(id);
		valoracion.setAutor(this.usuarioLogeado);
		videojuego.agregarValoracion(valoracion);
		repositorioValoraciones.save(valoracion);
		repositorioVideojuegos.save(videojuego);
		return "valoracion_guardada";	
	}
	
	@GetMapping("/videojuego/{id}/form_valoracion")
	public String mostrarForm(Model model, @PathVariable long id,HttpSession sesion){
		if(this.usuarioLogeado != null){
			model.addAttribute("id",id);
			return "form_valoracion";
		}else{
			return "logearse_necesita";
		}
	}
}

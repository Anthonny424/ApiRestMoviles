package utpDesarrolloMovil.demo.controller;



import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Item;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import utpDesarrolloMovil.demo.dto.*;
import utpDesarrolloMovil.demo.model.*;
import utpDesarrolloMovil.demo.service.*;


import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class UserController {


    @Autowired
    private IHorarios horariosservice;

    @Autowired
    private IEstacion estacionService;

    @Autowired
    private IUsuario usuarioService;

    @Autowired
    private ITarjeta tarjetaService;

    @Autowired
    private ITicket ticketService;

    @Autowired
    private MailService mailService;

    @Value("${mercadopago.access_token}")
    private String accessToken;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;


    //Requerimiento funcional 1 y 3 ver estaciones, horarios y servicios

    @GetMapping("estaciones")
    public ResponseEntity<List<EstacionDTO>> getAllEstaciones(){
        return ResponseEntity.status(HttpStatus.FOUND).body(estacionService.getEstaciones());
    }

    @GetMapping("horarios/{idestacion}")
    public  ResponseEntity<List<HorarioDTO>> getPorEstacion(@PathVariable int idestacion){
        return ResponseEntity.status(HttpStatus.FOUND).body(horariosservice.getByEstacion(idestacion));
    }


    //Requerimiento funcional 2 Recargar tarjeta

    @PostMapping("/auth/createAndRedirect")
    public ResponseEntity<RecargaResponse> createAndRedirect(@RequestBody RecargaRequest request) throws MPException {
        MercadoPago.SDK.setAccessToken(accessToken);
        Preference preference = new Preference();
        preference.setBackUrls(
          new BackUrls().setFailure("http://localhost:8080/api/v1/failure")
                  .setPending("http://localhost:8080/api/v1/pending")
                  .setSuccess("http://localhost:8080/api/v1/success?monto=" + request.getMonto() + "&iduser=" + request.getIduser() + "&idtarjeta=" + request.getIdtarjeta())
        );
        Item item = new Item();
        item.setTitle("Recarga Tarjeta")
                .setQuantity(1)
                .setUnitPrice((float) request.getMonto());
        preference.appendItem(item);
        var result = preference.save();
        System.out.println("Preference saved: " + result);

        String sandboxInitPoint = result.getSandboxInitPoint();
        System.out.println("Sandbox Init Point: " + sandboxInitPoint);

        RecargaResponse response = new RecargaResponse(sandboxInitPoint);

        System.out.println("Response: " + response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/success")
    public String success(HttpServletRequest request, HttpSession session,
                          @RequestParam("collection_id") String collection_id,
                          @RequestParam("collection_status")String collectionStatus,
                          @RequestParam("monto") double monto,
                            @RequestParam("iduser") int iduser,
                            @RequestParam("idtarjeta") int idtarjeta)
                           throws MPException{
        var payment = com.mercadopago.resources.Payment.findById(collection_id);
        System.out.println("monto: "+monto+" iduser: "+iduser+" idtarjeta: "+idtarjeta);


        Tarjeta tarjeta = tarjetaService.buscarPorIdNoDTO(idtarjeta);
        Usuario usuario = usuarioService.findUserByIdNotDto(iduser);

        Ticket ticket = new Ticket();
        ticket.setImporte(monto);
        ticket.setSaldoanterior(tarjeta.getSaldo());
        ticket.setNuevosaldo(monto+tarjeta.getSaldo());
        ticket.setFecha(new Date());
        ticket.setTarjeta(tarjeta);
        ticketService.guardarTicket(ticket);

        tarjeta.setId(idtarjeta);
        tarjeta.setSaldo(tarjeta.getSaldo()+monto);
        tarjeta.setUsuario(usuario);
        tarjeta.setTarifa(tarjeta.getTarifa());
        tarjeta.setNrotarjeta(tarjeta.getNrotarjeta());
        tarjetaService.guardarTarjeta(tarjeta);

        return "Recarga hecha!";
    }

    @GetMapping("/failure")
    public String failure(HttpServletRequest request) {
        System.out.println("El pago ha fallado.");
        String failureMessage = request.getParameter("message");
        System.out.println("Mensaje de fallo: " + failureMessage);
        return "Error en la compra.";
    }


    //Requerimiento funcional 4 API correo electrotico pa mandar sugerencias

    @PostMapping("/send")
    public ResponseEntity<String> enviarMensageSugerencia(@RequestBody EmailRequest email){
        mailService.sendMessageUser(email.getEmail(), email.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body("Enviado!");
    }


    //Requerimiento funcional 5 Poder crear, gestionar perfil, ver tarjeta, ver tus tickets

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest authLoginRequest){
        return new ResponseEntity<>(this.userDetailService.loginRequest(authLoginRequest), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<UsuarioDTO> crearUsuario(@Valid @RequestBody UsuarioCreateDTO nuevoUsuario) {
        UsuarioDTO usuarioDTO = usuarioService.SaveUsuarioCompleto(nuevoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDTO);
    }

    @GetMapping("/auth/tarjeta/{iduser}") //Retorna info. tarjeta, tarifa del
    public ResponseEntity<TarjetaDTO> getTarjeta(@PathVariable int iduser){
        TarjetaDTO tarjetaDTO = tarjetaService.buscarPorIdUserWithJPQL(iduser);
        return ResponseEntity.status(HttpStatus.FOUND).body(tarjetaDTO);
    }

    @GetMapping("/auth/tickets/{idtarjeta}")
    public ResponseEntity<List<TicketDTO>> getTickerPorId(@PathVariable int idtarjeta) {
        List<TicketDTO> ticketsDTO= ticketService.getTicketPorId(idtarjeta);
        return ResponseEntity.status(HttpStatus.FOUND).body(ticketsDTO);
    }

    //Actualizar usuario

    @GetMapping("/auth/user/{iduser}")
    public ResponseEntity<UsuarioDTO> BuscarInforUserById(@PathVariable int iduser){
        UsuarioDTO usuarioDTO= usuarioService.getUsuarioPorId(iduser);
        return ResponseEntity.status(HttpStatus.FOUND).body(usuarioDTO);
    }

    @PutMapping("/auth/update")
    public ResponseEntity<UsuarioDTO> actualizarUser(@RequestBody UsuarioCreateDTO usuario){ //solo enviar id username contra y nrotarjeta
        Usuario usuarioEncontrado = usuarioService.findUserByIdNotDto(usuario.getId());
        String username, contrasena;
        int nrotarjeta;
        username = usuario.getUsername();
        contrasena = usuario.getPassword();
        nrotarjeta = usuario.getTarjetaDTO().getNrotarjeta();
        UsuarioDTO usuarioActualizado = usuarioService.updateUsuario(usuarioEncontrado, username, contrasena, nrotarjeta);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuarioActualizado);
    }

}

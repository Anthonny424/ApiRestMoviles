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
import org.springframework.web.bind.annotation.*;
import utpDesarrolloMovil.demo.model.*;
import utpDesarrolloMovil.demo.service.*;


import java.net.URI;
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


    //Requerimiento funcional 1 y 3 ver estaciones, horarios y servicios

    @GetMapping("estaciones")
    public ResponseEntity<List<Estacion>> getAllEstaciones(){
        return ResponseEntity.status(HttpStatus.FOUND).body(estacionService.getEstaciones());
    }

    @GetMapping("horarios/{idestacion}")
    public  ResponseEntity<List<Horario>> getPorEstacion(@PathVariable int idestacion){
        return ResponseEntity.status(HttpStatus.FOUND).body(horariosservice.getByEstacion(idestacion));
    }


    //Requerimiento funcional 2 Recargar tarjeta

    @GetMapping("/auth/createAndRedirect/{monto}/{iduser}/{idtarjeta}")
    public ResponseEntity<Void> createAndRedirect(@PathVariable double monto, @PathVariable int iduser, @PathVariable int idtarjeta, HttpSession session) throws MPException {
        session.setAttribute("monto", monto);
        session.setAttribute("iduser", iduser);
        session.setAttribute("idtarjeta", idtarjeta);
        MercadoPago.SDK.setAccessToken(accessToken);
        Preference preference = new Preference();
        preference.setBackUrls(
          new BackUrls().setFailure("http://localhost:8080/api/v1/failure")
                  .setPending("http://localhost:8080/api/v1/pending")
                  .setSuccess("http://localhost:8080/api/v1/success")
        );
        Item item = new Item();
        item.setTitle("Recarga Tarjeta")
                .setQuantity(1)
                .setUnitPrice((float) monto);
        preference.appendItem(item);
        var result = preference.save();
        System.out.println("Preference saved: " + result);

        String sandboxInitPoint = result.getSandboxInitPoint();
        System.out.println("Sandbox Init Point: " + sandboxInitPoint);

        return ResponseEntity.status(302)
                .location(URI.create(sandboxInitPoint))
                .build();
    }

    @GetMapping("/success")
    public String success(HttpServletRequest request, HttpSession session,
                          @RequestParam("collection_id") String collection_id,
                          @RequestParam("collection_status")String collectionStatus
                          ) throws MPException{
        var payment = com.mercadopago.resources.Payment.findById(collection_id);

        double monto = (Double) session.getAttribute("monto");
        int iduser = (Integer) session.getAttribute("iduser");
        int idtarjeta = (Integer) session.getAttribute("idtarjeta");
        Tarjeta tarjeta = tarjetaService.buscarPorIdWithJPQL(idtarjeta).get();
        Usuario usuario = usuarioService.getUsuarioPorId(iduser);

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

        session.removeAttribute("monto");
        session.removeAttribute("iduser");
        session.removeAttribute("idtarjeta");

        return "Recarga hecha!";
    }

    @GetMapping("/failure")
    public String failure(HttpServletRequest request) {
        System.out.println("El pago ha fallado.");
        String failureMessage = request.getParameter("message");
        System.out.println("Mensaje de fallo: " + failureMessage);
        return "error";
    }


    //Requerimiento funcional 4 API correo electrotico pa mandar sugerencias

    @PostMapping("/send")
    public ResponseEntity<String> enviarMensageSugerencia(@RequestBody EmailRequest email){
        mailService.sendMessageUser(email.getEmail(), email.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body("Enviado!");
    }


    //Requerimiento funcional 5 Poder crear, gestionar perfil, ver tarjeta, ver tus tickets


    @PostMapping("/create")
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario nuevoUsuario) {
        Usuario usuario = usuarioService.SaveUsuarioCompleto(nuevoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping("/auth/tarjeta/{idtarjeta}") //Retorna info. tarjeta, tarifa del
    public ResponseEntity<Tarjeta> getTarjeta(@PathVariable int idtarjeta){
        return ResponseEntity.status(HttpStatus.FOUND).body(tarjetaService.buscarPorIdWithJPQL(idtarjeta).get());
    }

    @GetMapping("/auth/tickets/{idtarjeta}")
    public ResponseEntity<List<Ticket>> getTickerPorId(@PathVariable int idtarjeta) {
        List<Ticket> tickets= ticketService.getTicketPorId(idtarjeta);
        return ResponseEntity.status(HttpStatus.FOUND).body(tickets);
    }

    //Actualizar usuario

    @GetMapping("/auth/user/{iduser}")
    public ResponseEntity<Usuario> BuscarInforUserById(@PathVariable int iduser){
        Usuario usuario= usuarioService.getUsuarioPorId(iduser);
        return ResponseEntity.status(HttpStatus.FOUND).body(usuario);
    }

    @PutMapping("/auth/update")
    public ResponseEntity<Usuario> actualizarUser(@RequestBody Usuario usuario){ //solo enviar id username contra y nrotarjeta
        Usuario usuarioEncontrado = usuarioService.getUsuarioPorId(usuario.getId());
        String username, contrasena;
        int nrotarjeta;
        username = usuario.getUsername();
        contrasena = usuario.getContrasena();
        nrotarjeta = usuario.getTarjeta().getNrotarjeta();
        Usuario usuarioActualizado = usuarioService.updateUsuario(usuarioEncontrado, username, contrasena, nrotarjeta);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuarioActualizado);
    }

}

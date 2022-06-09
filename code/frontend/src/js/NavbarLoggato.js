import {Link} from 'react-router-dom'; //se usi link invece di <a href> non passi per il server
import '../css/NavbarLoggato.css';
import { useCookies } from 'react-cookie';
import axios from 'axios';
axios.defaults.withCredentials = true;

const NavbarLoggato = (props) => {

    const [cookies, setCookie] = useCookies(); //variabile che gestisce i cookie

    const clickBottone = async (e) => {
        const url = 'http://localhost:9000/logout';
        await axios.post(url);
        
        //cancello i cookie
        setCookie('testo', "",{path: '/home'});
        setCookie('stato', "",{path: '/home'});
        setCookie('from', "",{path: '/home'});
        setCookie('da', "",{path: '/home'});
        setCookie('scegli', "",{path: '/home'});
        
        window.location.href = "http://localhost:3000";
    }

    const pulsanti = [ {//variabile che contiene i vari pulsanti
            pulsante: <td><Link className='navbarloggato' to='/home'>Home</Link></td>
    }];

    if (props.professione != "cliente") // se è un tecnico metto il pulsante per la pagina filtra
        pulsanti.push({pulsante: <td><Link className="navbarloggato" to='/filtra'>Filtra</Link></td>});
        
    if (props.professione == "admin") { // se è l'admin metto la possibilità di aggiungere, eliminare un utente
        pulsanti.push({pulsante: <td><Link className="navbarloggato" to='/registra'>Registra</Link></td>});
        pulsanti.push({pulsante: <td><Link className="navbarloggato" to='/elimina'>Elimina</Link></td>});
    }

    pulsanti.push({pulsante: <td><Link className="navbarloggato" to='/' onClick = {clickBottone}>Logout</Link></td>});

    return ( 
        <nav className="navbar">
            <div className="divLink">
                <table>
                    <tbody>
                        <tr>
                            
                            {pulsanti.map(icona => icona.pulsante)}
                                          
                        </tr>
                    </tbody>
                </table>
            </div>
        </nav>
    );
}
 
export default NavbarLoggato;
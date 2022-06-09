import { sha3_512 } from 'js-sha3';
import { useRef } from 'react';
import {useEffect} from 'react';
import {useState} from 'react';
import '../css/Form.css';
import '../css/Sfondo.css';
import axios from 'axios';
axios.defaults.withCredentials = true;

const Signup = (props) => {

    const [titolo, setTitolo] = useState('');

    useEffect(async () => { //una volta caricata la pagina
        if (props.professione === "cliente") {// se sono nella signup non faccio niente
            setTitolo("Registrati come cliente");
            return;
        }
        setTitolo("Registra un nuovo tecnico");

        const url = "http://localhost:9000/verifica"; //url al server java
        const risposta = await axios.post(url);
        if (risposta.data.professione !== "admin") { // se non sono admin non posso accedere
        
            window.location.href = "http://localhost:3000/home";
        }
    });

    const emailInput = useRef();
    const nomeInput = useRef();
    const cognomeInput = useRef();
    const passwordInput = useRef();
    const password2Input = useRef();

    const handleLoginForm = async (e) => {
        e.preventDefault(); //evita di ricaricare la pagina
        const url = "http://localhost:9000/signup";

        const email = emailInput.current.value;
        const nome = nomeInput.current.value;
        const cognome = cognomeInput.current.value;
        const password2 = password2Input.current.value;
        const professione = props.professione;
        let password = passwordInput.current.value;

        if (password !== password2) {
            alert("le due password devono essere uguali");
            return;
        }

        password = sha3_512(password); //cifro la password

        const corpo = {
            nome: nome,
            cognome: cognome,
            email: email, 
            password: password,
            professione: professione,
            withCredentials: true
        }; //creo l'oggetto json da inviare al server

        //INVIO I DATI
        const risposta = await axios.post(url, corpo);

        //guardo cosa è successo
        if (risposta.data.id) {
            alert("Utente inserito correttamente");

            if (professione !== "cliente") //se non è un cliente allora non eseguo il login
                return;
            
            //faccio il login
            const corpo2 = {email: email, password: password, withCredentials: true};
            const url2 = "http://localhost:9000/login";
            const risposta2 = await axios.post(url2, corpo2);

            //guardo se è tutto ok
            if (risposta.data.id) {
                const home = window.location.href.replace("/signup", "/");
                window.location.href = home + "home";
            }
            else
                alert(risposta.data.errore);
        }
        else
            alert(risposta.data.errore);
    }

    return ( 
        <div className="divSignup">
            <h1>{titolo}</h1>
            <form onSubmit = {handleLoginForm}>
                <input className='testo' type='text' id='nome' name='nome' placeholder='Nome' ref={nomeInput} required /> <br /><br />
                <input className='testo' type='text' id='cognome' name='cognome' placeholder='Cognome' ref={cognomeInput} required /> <br /><br />
                <input className='testo' type='email' id='email' name='email' placeholder='Email' ref={emailInput} required /><br /><br />
                <input className='testo' type='password' id='password' name='password' minLength={Number('8')} placeholder='Password' ref={passwordInput} required /> <br /><br />
                <input className='testo' type='password' id='password2' name='password2' minLength={Number('8')} placeholder='Reinserisci la password' ref={password2Input} required /> <br /><br />
                <br />
                <br />
                <button id='bottone' type='submit'> Invia</button>
            </form>
        </div>
    );
}
 
export default Signup;
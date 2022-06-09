import '../css/Form.css';
import '../css/Sfondo.css';
import { sha3_512 } from 'js-sha3';
import { useRef } from 'react';
import axios from 'axios';
axios.defaults.withCredentials = true;

const Login = () => {
    
    //servono per inviare i dati nel form
    const emailInput = useRef();
    const passwordInput = useRef();

    const handleLoginForm = async (e) => {
        e.preventDefault(); //evita di ricaricare la pagina
        const url = "http://localhost:9000/login"; //url al server java

        const email = emailInput.current.value; //prendo il valore dell'input text
        let password = passwordInput.current.value;
        password = sha3_512(password); //cifro la password

        const corpo = {email: email, password: password, withCredentials: true}; //creo l'oggetto json da inviare al server
        
        //INVIO I DATI
        const risposta = await axios.post(url, corpo);

        //guardo cosa è successo
        if (risposta.data.id) {
            alert("Autenticazione avvenuta con successo");
            window.location.href = window.location.href + "home";
        }
        else
            alert(risposta.data.errore);
    }

    return (
        <div className="divLogin">
            <h1>Login</h1>

            <form onSubmit = {handleLoginForm}>
                <input className='testo' type='text' ref={emailInput} id='email' name='email' placeholder='Email' required /> <br /> <br />
                <input className='testo' type='password' ref={passwordInput} id='password' name='password' placeholder='Password' required /> <br /> <br />
                <button id='bottone' type='submit'> Invia</button>
            </form>
        </div> 
    );
}
 
export default Login;
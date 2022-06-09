import { useRef } from 'react';
import {useEffect} from 'react';
import axios from 'axios';
axios.defaults.withCredentials = true;

const Elimina = () => {

    //servono per inviare i dati nel form
    const emailInput = useRef();

    useEffect(async () => { //una volta caricata la pagina

        const url = "http://localhost:9000/verifica"; //url al server java
        const risposta = await axios.post(url);
        if (risposta.data.professione != "admin") { // se non sono admin non posso accedere
        
            window.location.href = "http://localhost:3000/home";
        }
    });

    const handleLoginForm = async (e) => {
        e.preventDefault(); //evita di ricaricare la pagina
        const url = "http://localhost:9000/elimina"; //url al server java

        const email = emailInput.current.value; //prendo il valore dell'input text

        const corpo = {email: email, withCredentials: true}; //creo l'oggetto json da inviare al server

        //INVIO I DATI
        const risposta = await axios.post(url, corpo);

        alert(risposta.data.errore);
    }

    return ( 
        <div className="elimina">
            <h1>Elimina un utente</h1>

            <form onSubmit = {handleLoginForm}>
                <input className='testo' type='text' ref={emailInput} id='email' name='email' placeholder='Email' required /> <br /> <br />
                <button id='bottone' type='submit'> Invia</button>
            </form>
        </div>
    );
}
 
export default Elimina;
import Filtra from './Filtra';
import costruisciLog from '../funzioni/log';
import costruisciSpiegazione from '../funzioni/spiegazione';
import {useEffect, useState} from 'react';
import parse from 'html-react-parser';
import axios from 'axios';
import "../css/Log.css";
axios.defaults.withCredentials = true;

const PagFiltra = (props) => {

    //dati da mandare alla pagina del filtro
    const [tabella, setTabella] = useState('');

    //dati per la tabella delle spiegazioni
    const [spiegazione, setSpiegazione] = useState('');

    useEffect(async () => { //una volta caricata la pagina
        const url = "http://localhost:9000/verifica"; //url al server java
        const risposta = await axios.post(url);
        if (risposta.data.professione == "cliente")  // se sei un cliente non puoi accedere
            window.location.href = "http://localhost:3000/home";

        setTabella(costruisciLog(props.dati));
        
    }, []);

    function handleClick(e)  {
        e.preventDefault(); 
        const oggetto = JSON.parse(props.dati);
        const id = e.target.id+""; // cosi è una stringa

        if (id[0] == "a") { // se è un errore (gli id iniziano per a)
            const visualizza = oggetto.err.filter(err => "a"+err.id == id);
            
            setSpiegazione(costruisciSpiegazione(visualizza[0], "errore"));
        }
        else {
            const visualizza = oggetto.log.filter(log => log.id == id);

            setSpiegazione(costruisciSpiegazione(visualizza[0], "buono"));
        }
    }

    return ( 
        <div className="pagfiltra">
            <table>
                <tbody>
                    <tr>
                        <td className="tabellaLog">
                            <div className="log" onClick={handleClick}>
                                {parse(tabella)}
                            </div>
                        </td>
                        <td className="tabellaLog">
                            <div className="log">
                                {parse(spiegazione)}
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <Filtra />
        </div>
    );
}
 
export default PagFiltra;
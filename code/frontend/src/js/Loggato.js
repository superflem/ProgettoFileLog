import PaginaNonTrovata from './PaginaNonTrovata';
import Home from './Home';
import NavbarLoggato from './NavbarLoggato';
import PagFiltra from './PagFiltra';
import Signup from './Signup';
import Elimina from './Elimina';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'; //servono per andare in diverse pagine
import {useEffect} from 'react';
import {useState} from 'react';
import datiGrafici from '../funzioni/datiGrafici';
import defaults from '../funzioni/defaultGrafici';
import getCookie from '../funzioni/getCookie';
import axios from 'axios';
axios.defaults.withCredentials = true;


const Loggato = () => {

   //costruisco il grafico delle comunicazioni di default
   const [datiGraficoComunicazioni, setDatiGraficoComunicazioni] = useState(defaults.comunicazioniDefault);

    //costruisco il grafico dei posti di default
    const [datiGraficoPosti, setDatiGraficoPosti] = useState(defaults.postiDefault);

    //costruisco il grafico dei posti di default
    const [datiGraficoBytes, setDatiGraficoBytes] = useState(defaults.bytesDefault);

    //nome e professione per verificare che tutto sia ok
    const [nome, setNome] = useState('');
    const [professione, setProfessione] = useState('');
    
    //dati da mandare alla pagina del filtro
    const [dati, setDati] = useState('');

    useEffect(async () => { //una volta caricata la pagina
        const url = "http://localhost:9000/verifica"; //url al server java, controllo di essere loggato
        const risposta = await axios.post(url);
        if (!risposta.data.nome)      
            window.location.href = "http://localhost:3000";
        else {
            setNome(risposta.data.nome);
            setProfessione(risposta.data.professione);
        }
        
        //prendo i campi dai cookie
        var testo = getCookie('testo');
        var stato = getCookie('stato');
        var from = getCookie('from');
        var to = getCookie('da');
        var scegli = getCookie('scegli');

        //controllo che i campi siano diversi da null
        if (testo === null) testo = "";
        if (stato === null) stato = "";
        if (from === null || from === "") from = Math.round(new Date().getTime() / 1000) - 90000;
        if (to === null || to === "") to = Math.round(new Date().getTime() / 1000) + 90000;
        if (scegli === null) scegli = "";

        const url2 = "http://localhost:9000/query";
        const corpo = {testo: testo, stato: stato, from: from, to: to, scegli: scegli, withCredentials: true};

        const risposta2 = await axios.post(url2, corpo);

        var comunicazioni, posti, bytes; //creo i grafici e li passo al componente appropriato
        [comunicazioni, posti, bytes] = datiGrafici (risposta2);
        setDatiGraficoComunicazioni(comunicazioni);
        setDatiGraficoPosti(posti);
        if (bytes !== "fallito")
            setDatiGraficoBytes(bytes);

        setDati(JSON.stringify(risposta2.data));
    }, []);
    
    return ( 
        <Router>
            <div className="loggato">
                <NavbarLoggato professione={professione}/>

                <Switch>

                    <Route exact path='/home'>
                        <Home nome={nome} datiGraficoComunicazioni={datiGraficoComunicazioni} datiGraficoPosti={datiGraficoPosti} datiGraficoBytes={datiGraficoBytes}/>
                    </Route>

                    <Route exact path='/filtra'>
                        <PagFiltra dati={dati} />
                    </Route>

                    <Route exact path='/registra'>
                        <Signup professione = "tecnico"/>
                    </Route>

                    <Route exact path='/elimina'>
                        <Elimina />
                    </Route>

                    <Route path='*'>  {/* una qualsiasi altra pagina*/}
                        <PaginaNonTrovata />
                    </Route>
                    
                </Switch>
            </div>
        </Router>
    );
}
 
export default Loggato;
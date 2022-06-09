import { MapContainer, TileLayer, useMap, Marker, Popup } from 'react-leaflet';
import {useEffect, useState} from 'react';
import '../css/Mappa.css';
import costruisciMarker from '../funzioni/marker';
import getCookie from '../funzioni/getCookie';
import axios from 'axios';
axios.defaults.withCredentials = true;

const Mappa = () => {

    const [marker, setMarker] = useState([]);

    useEffect(async () => { //una volta caricata la pagina

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


        setMarker(costruisciMarker(risposta2.data));

    }, []);
    

    return (

        <div className="mappa">
            
            <MapContainer center={[11.505, -0.09]} zoom={1} scrollWheelZoom={false} className="map" id="negro">
                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                
                {marker.map(singolo => (
                    <Marker position={singolo.posizione} >
                        <Popup>
                            {singolo.paese}
                        </Popup>
                    </Marker>
                ))}
            </MapContainer>
        </div>
    );
}

export default Mappa;
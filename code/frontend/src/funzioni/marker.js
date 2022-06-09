import capitali from './capitali.js';

const costruisciMarker = function (oggetto) {
    const inseriti = []; //array che verifica se un marker è già stato inserito o no
    var marker = []; //variabile che conterra i dati dei marker

    //per i log
    oggetto.log.forEach (log => {
        if (log.paese !== "null" && log.paese !== null) { //devo verificare che il paese esista
            if (inseriti.indexOf(log.paese) === -1) { //se non c'è gia il marker
                inseriti.push(log.paese); //inserisco il nuovo paese

                const capitaleSpecifica = capitali.table.tr.filter(cap => cap.td[0] === log.paese); //trovo la capitale corrispondente al codice dello stato e la inserisco
                const latitudine = capitaleSpecifica[0].td[1];
                const longitudine = capitaleSpecifica[0].td[2];
                const paese = capitaleSpecifica[0].td[3];

                marker.push ({"posizione": [latitudine, longitudine], "paese": paese});
            }
        }
    });

    //per gli errori
    oggetto.err.forEach (err => {
        if (err.paese !== "null" && err.paese !== null) { //devo verificare che il paese esista
            if (inseriti.indexOf(err.paese) === -1) { //se non c'è gia il marker
                inseriti.push(err.paese); //inserisco il nuovo paese

                const capitaleSpecifica = capitali.table.tr.filter(cap => cap.td[0] === err.paese); //trovo la capitale corrispondente al codice dello stato e la inserisco
                const latitudine = capitaleSpecifica[0].td[1];
                const longitudine = capitaleSpecifica[0].td[2];
                const paese = capitaleSpecifica[0].td[3];

                marker.push ({"posizione": [parseFloat(latitudine), parseFloat(longitudine)], "paese": paese});
            }
        }
    });

    return marker;
}

export default costruisciMarker;
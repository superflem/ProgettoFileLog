function ordina(unione) {
    for (var i = 0; i < unione.length-1; i++) {
        for (var j = i+1; j < unione.length; j++) {
            if (unione[i].data > unione[j].data) {
                const tmp = unione[i];
                unione[i] = unione[j];
                unione[j] = tmp;
            }
        }
    }
    return unione;
}


const costruisciLog = function (dati) {
    var tabella = "";
    const oggetto = JSON.parse(dati);


    var unione = [];
    oggetto.log.forEach(log => {
        unione.push({ "timestamp": log.timestamp, "paese": log.paese, "tipo": "buono", "data": log.data, "id": log.id });
    });
    oggetto.err.forEach(err => {
        unione.push({ "timestamp": err.timestamp, "paese": err.paese, "tipo": "errore", "data": err.data, "id": "a"+err.id });
    });

    unione = ordina(unione); //ordino gli elementi per data

    for (var i = 0; i < unione.length; i++) {
        if (i === 0) //se è il primo
            tabella += "<p id='"+unione[i].id+"' className='blu primo " + unione[i].tipo + "'>";
        else if (i === unione.length - 1) // se è l'ultimo
            tabella += "<p id='"+unione[i].id+"' className='blu ultimo " + unione[i].tipo + "'>";
        else //quelli in mezzo
            tabella += "<p id='"+unione[i].id+"' className='blu " + unione[i].tipo + "'>";

        tabella += unione[i].timestamp + ", " + unione[i].paese;
        tabella += "</p>";
    }


    return tabella;
}

export default costruisciLog
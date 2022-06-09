function buoni (dato, testo) {
    testo = "<p className='buono'>";
    testo += "<b>Id: </b> "+dato.id+" <br />";
    testo += "<b>Request: </b> "+dato.request+" <br />";
    testo += "<b>Auth: </b> "+dato.auth+" <br />";
    testo += "<b>Ident: </b> "+dato.ident+" <br />";
    testo += "<b>HttpMethod: </b> "+dato.httpmethod+" <br />";
    testo += "<b>Time: </b> "+dato.time+" <br />";
    testo += "<b>Response: </b> "+dato.response+" <br />";
    testo += "<b>Bytes: </b> "+dato.bytes+" <br />";
    testo += "<b>Client Ip: </b> "+dato.clientip+" <br />";
    testo += "<b>RawRequest: </b> "+dato.rawrequest+" <br />";
    testo += "<b>Data: </b> "+dato.data+" <br />";
    testo += "<b>Timestamp: </b> "+dato.timestamp+" <br />";
    testo += "<b>Paese: </b> "+dato.paese+" <br />";

    return testo
}

function cattivi (dato, testo) {
    testo = "<p className='errore'>";
    testo += "<b>Id: </b> "+dato.id+" <br />";
    testo += "<b>Tipo Errore: </b> "+dato.tipoErrore+" <br />";
    testo += "<b>Pid: </b> "+dato.pid+" <br />";
    testo += "<b>Client Ip: </b> "+dato.clientip+" <br />";
    testo += "<b>Porta Client: </b> "+dato.portaClient+" <br />";
    testo += "<b>Error Code: </b> "+dato.errorCode+" <br />";
    testo += "<b>Data: </b> "+dato.data+" <br />";
    testo += "<b>Timestamp: </b> "+dato.timestamp+" <br />";
    testo += "<b>Paese: </b> "+dato.paese+" <br />";
    testo += "<b>Payload: </b> "+dato.payload+" <br />";
    return testo
}

const costruisciSpiegazione = function (dato, tipo) {
    var testo = "";

    if (tipo == "buono")
        testo += buoni(dato, testo);
    else
        testo += cattivi(dato, testo);

    testo += "</p>";

    return testo;
}

export default costruisciSpiegazione;
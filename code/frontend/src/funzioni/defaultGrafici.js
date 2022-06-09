const comunicazioniDefault = {
    labels: ["Default"], //lista delle labels della torta
    datasets: [{
        label: "Comunicazioni",
        data: [1], //dati che usa per creare il grafico
        backgroundColor: ["black"] //colora le barre
    }]
};

const postiDefault = {
    labels: ["Default"], //lista delle labels della torta
    datasets: [{
        label: "Posti",
        data: [1], //dati che usa per creare il grafico
        backgroundColor: ["black"] //colora le barre
    }]
};

const bytesDefault = {
    labels: ["Default"], //lista delle labels della torta
    datasets: [{
        label: "Bytes",
        data: [1], //dati che usa per creare il grafico
        backgroundColor: ["black"] //colora le barre
    }]
};

const defaults = {
    "comunicazioniDefault": comunicazioniDefault,
    "postiDefault": postiDefault,
    "bytesDefault": bytesDefault
}

export default defaults;
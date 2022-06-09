import '../css/Form.css';
import '../css/Filtra.css';
import { useRef } from 'react';
import { useCookies } from 'react-cookie';

const Filtra = () => {

    const testoInput = useRef();
    const statoInput = useRef();
    const data1Input = useRef();
    const data2Input = useRef();
    const benevoliInput = useRef();

    const [cookies, setCookie] = useCookies(); //variabile che gestisce i cookie

    const handleLoginForm = async (e) => {
        e.preventDefault(); //evita di ricaricare la pagina

        const testo = testoInput.current.value;
        const stato = statoInput.current.value;
        var data1Stringa = data1Input.current.value;
        var data2Stringa = data2Input.current.value;
        const scegli = benevoliInput.current.value;

        if (data1Stringa !== "" && data2Stringa !== "") { //controllo che la data1 sia minore della data2
            if (data1Stringa > data2Stringa) {
                const tmp = data1Stringa;
                data1Stringa = data2Stringa;
                data2Stringa = tmp;
            }
        }
        else if (data1Stringa !== "") //se una delle due è vuota, allora metto quella null uguale all'altra
            data2Stringa = data1Stringa;
        else
            data1Stringa = data2Stringa;
        
        var from, to;
        from = new Date(data1Stringa).getTime() / 1000; // creo l'oggetto data e stampo i secondi passati dal 1970
        to = new Date(data2Stringa).getTime() / 1000; // creo l'oggetto data e stampo i secondi passati dal 1970

        if (isNaN(from)) { //se non metto la data, automaticamente prende la data di oggi
            from = Math.round(new Date().getTime() / 1000);
            to = Math.round(new Date().getTime() / 1000); 
        }
        
        //setto i cookie
        setCookie('testo', testo,{path: '/home'});
        setCookie('stato', stato,{path: '/home'});
        setCookie('from', from,{path: '/home'});
        setCookie('da', to,{path: '/home'});
        setCookie('scegli', scegli,{path: '/home'});
        
        window.location.href = "http://localhost:3000/home"; //rimando a /home dove farà la query
    }

    return ( 
        <div className="filtra">
            <h2>Filtra le comunicazioni</h2>
            <form onSubmit = {handleLoginForm}>
                <input className='testo' type='text' id='testo' name='testo' placeholder='Testo' ref={testoInput}  /> <br /><br />
                <input className='testo' type='text' id='stato' name='stato' placeholder='Stato' ref={statoInput}  /> <br /><br />
                <input type="date" id="data1" name="data1" ref={data1Input} min="1970-01-01"/> <br /> <br />
                <input type="date" id="data2" name="data2" ref={data2Input} /> <br /> <br />
                <select name="benevoli" id="benevoli" ref={benevoliInput}>
                    <option value="">Scegli</option>
                    <option value="buono">Avvenute</option>
                    <option value="errore">Errore</option>
                </select> <br /> <br />
                <button id='bottoneFiltra' type='submit'> Invia</button>
            </form>
        </div>
    );
}
 
export default Filtra;
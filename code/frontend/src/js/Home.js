import Mappa from './Mappa';
import Grafici from './Grafici';
import '../css/tabella.css';
import '../css/Home.css';

const Home = (props) => {

    return ( 
        <div className="home">
            <h1>Benvenuto, {props.nome}</h1>
            
            <table>
                <tbody>
                    <tr>
                        <td>
                            <Mappa/>
                        </td>
                        <td>
                            <Grafici datiGraficoPosti={props.datiGraficoPosti} datiGraficoComunicazioni={props.datiGraficoComunicazioni} datiGraficoBytes={props.datiGraficoBytes}/>
                        </td>
                    </tr>
                </tbody>
            </table>
            
            
           
                        
        </div>
     );
}
 
export default Home;
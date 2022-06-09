import { useState } from "react";
import { Pie } from "react-chartjs-2";
import { Chart as ChartJS } from "chart.js/auto"; //questo serve per mostrare il grafico
import {useEffect} from 'react';
import "../css/Grafici.css";
import axios from 'axios';
axios.defaults.withCredentials = true;


const Grafici = (props) => {

    //colora le label
    const labels = {
        plugins: {
            legend: {
                display: true,
                labels: {
                    color: "white",
                    font: {
                        size: 20
                    }
                }
            }
        }
    }
    
    return ( 
        <div className="grafici">
            <table id="tabellaGrafici">
                <tbody>
                    <tr>
                        <td className="cellaGrafici">
                            <Pie data={props.datiGraficoComunicazioni} className="torta" options = {labels}/>; {/* grafico delle comunicazioni */}
                        </td>
                        <td className="cellaGrafici">
                            <Pie data={props.datiGraficoPosti} className="torta" options = {labels}/>; {/* grafico delle comunicazioni */}
                        </td>  
                    </tr>
                </tbody>
            </table>
            <Pie id="bytes" data={props.datiGraficoBytes} className="torta" options = {labels}/>; {/* grafico delle comunicazioni */}
        </div>
    );
}
 
export default Grafici;
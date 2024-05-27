import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import moment from 'moment';
import PropTypes from 'prop-types';

import {useTheme} from '@mui/material/styles';
CustomTooltip.propTypes = {
    payload: PropTypes.array,
    label: PropTypes.string,
    active: PropTypes.bool
};

function CustomTooltip ({ payload, label, active }) {

    const theme = useTheme();
    if (active && payload && payload.length) {
        return (
            <div className="custom-tooltip" style={{ backgroundColor: theme.palette.primary.dark, borderRadius: '10px', border: '1px solid white', padding: '0px 25px',  }}>
                <p className="label">{`Date: ${moment(label).format("DD.MM.YYYY HH:mm")}`}</p>
                <p className="intro">{`Balance: $${payload[ 0 ].value}`}</p>
            </div>
        );
    }

    return null;
}



export default function PortfolioValueHistoryChart( pros) {
    
    const { data } = pros;
    
    if (!data) return null;
    const data2 = data.map(item => ({
        date: new Date(item.dateTime),
        priceUSD: item.value
    }));
    

    const minX = Math.min(...data2.map(item => item.date));
    const maxX = Math.max(...data2.map(item => item.date));
    const minY = Math.min(...data2.map(item => item.priceUSD));
    const maxY = Math.max(...data2.map(item => item.priceUSD));

    return (
        <>
            <ResponsiveContainer >
                <LineChart data={data2}>
                    <XAxis dataKey="date" domain={[ minX, maxX ]} hide={true} type="number" scale="time"></XAxis>
                    <YAxis dataKey="priceUSD" domain={[ minY, maxY ]} hide={true} type="number" 
                            tickFormatter={(value) => (value / Math.pow(10, 9)).toFixed(9)}
                    ></YAxis>

                    <Tooltip position={{ x: -240 }} content={<CustomTooltip />} />
                    <Line type="monotone" isAnimationActive={false} dataKey="priceUSD" stroke="white" strokeWidth={2} dot={false} />
                </LineChart>
            </ResponsiveContainer>
        </>
    )
}

import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import moment from 'moment';
import PropTypes from 'prop-types';

CustomTooltip.propTypes = {
    payload: PropTypes.array,
    label: PropTypes.string,
    active: PropTypes.bool
};

function CustomTooltip ({ payload, label, active }) {
    if (active && payload && payload.length) {
        return (
            <div className="custom-tooltip" style={{ backgroundColor: "#3f51b5", borderRadius: '10px', border: '1px solid white', padding: '0px 25px',  }}>
                <p className="label">{`Date: ${moment(label).format("DD.MM.YYYY HH:mm")}`}</p>
                <p className="intro">{`Price: ${payload[ 0 ].value}`}</p>
            </div>
        );
    }

    return null;
}

SymbolDataTableChart.propTypes = {
    data: PropTypes.array.isRequired
};

function SymbolDataTableChart ({ data }) {

    const data2 = data.map(item => ({
        date: new Date(item.timestamp),
        priceUSD: item.priceUSD
    }));

    console.log(data2)

    const minX = Math.min(...data2.map(item => item.date));
    const maxX = Math.max(...data2.map(item => item.date));
    const minY = Math.min(...data2.map(item => item.priceUSD));
    const maxY = Math.max(...data2.map(item => item.priceUSD));
    console.log(minX, maxX, minY, maxY)

    return (
        <>
            <ResponsiveContainer width={200} height={50}>
                <LineChart data={data2}>
                    <XAxis dataKey="date" domain={[ minX, maxX ]} hide={true} type="number" scale="time"></XAxis>
                    <YAxis dataKey="priceUSD" domain={[ minY, maxY ]} hide={true} type="number" 
                            tickFormatter={(value) => (value / Math.pow(10, 9)).toFixed(9)}
                    ></YAxis>

                    <Tooltip position={{ x: -240 }} content={<CustomTooltip />} />
                    <Line type="monotone" isAnimationActive={false} dataKey="priceUSD" stroke="#8884d8" strokeWidth={2} dot={false} />
                </LineChart>
            </ResponsiveContainer>
        </>
    )
}

export default SymbolDataTableChart;
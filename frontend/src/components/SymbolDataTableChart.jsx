import React, { Component } from 'react';
import Chart from 'react-apexcharts'

class SymbolDataTableChart extends Component {
    constructor(props) {
        super(props);

        this.state = {
            options: {
                chart: {
                    id: 'line',
                    toolbar: {
                        show: false
                    }
                },
                stroke: {
                    width: [ 4, 0, 0 ]
                },
                xaxis: {
                    categories: [ 1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999 ],
                    labels: {
                        show: false, // This line hides the x-axis labels
                    },
                    axisBorder: {
                        show: false, // This line hides the x-axis line
                    },
                    axisTicks: {
                        show: false, // This line hides the x-axis ticks
                    },

                },
                yaxis: {
                    labels: {
                        show: false, // This line hides the y-axis labels
                    },
                    axisBorder: {
                        show: false, // This line hides the y-axis line
                    },
                    axisTicks: {
                        show: false, // This line hides the y-axis ticks
                    },
                },
                grid: {
                    show: false,
                },
                legend: {
                    show: false,
                },
                markers: {
                    size: 0,
                },
                dataLabels: {
                    enabled: false
                  },

            },
            series: [ {
                name: 'series-1',
                type: "line",
                data: [ 30, 40, 35, 50, 49, 60, 70, 91, 125 ]
            } ]
        }

    }
    render () {
        return (
            <div>
            <Chart options={this.state.options} series={this.state.series} type="bar" width={100} height={100} />
            </div>
        )
    }
}

export default SymbolDataTableChart;
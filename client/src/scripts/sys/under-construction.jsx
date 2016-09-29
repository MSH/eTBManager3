import React from 'react';
import { Card, Fa } from '../components';

export default class UnderConstruction extends React.Component {

    render() {
        return (
            <Card style={{ backgroundColor: '#000', color: '#fff' }}>
                <div className="text-center">
                    <Fa icon="hand-paper-o" size={3} />
                    <h1>{'Under construction'}</h1>
                    <p>
                        {'This page is stil under construction. Please come back later'}
                    </p>
                </div>
            </Card>
            );
    }
}

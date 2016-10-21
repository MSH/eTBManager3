/**
 * React component that displays a side bar - a verftical bar aligned to the left taking the whole space
 *
 *  @author Ricardo Memoria
 *  nov-2015
 */

import React from 'react';
import { Badge } from 'react-bootstrap';
import { isFunction } from '../commons/utils';


// load style
import './sideview.less';

export default class Sideview extends React.Component {

    hash(item) {
        const hash = this.props.route.path + item.path;

        // check if there is any query params
        let qry = this.props.queryParams;
        if (isFunction(qry)) {
            qry = qry(item);
        }

        qry = qry ?
            '?' + Object.keys(qry).map(p => p + '=' + encodeURIComponent(qry[p])).join('&') :
            '';

        return '#' + hash + qry;
    }

    /**
     * Get the selected item
     */
    getSelected() {
        const forpath = this.props.route.forpath;
        const items = this.props.views;

        return forpath ?
            items.find(it => it.path === forpath) :
            items.find(it => it.default);
    }

    render() {
        // get the items to fill in the sidebar
        const views = this.props.views;

        if (!views) {
            return null;
        }

        return (
            <ul className="sideview nav">
                {views.map((item, index) => {
                    if (item.noResultMsg) {
                        const title = item.title ? item.title : <hr/>;

                        return (
                            <span key={index}>
                                <li className="disabled">{title}</li>
                                <div className="message-muted">
                                    <i className={'fa fa-fw fa-' + item.icon} />
                                    <div>{item.noResultMsg}</div>
                                </div>
                            </span>
                            );
                    }

                    if (!item.path) {
                        const title = item.title ? item.title : <hr/>;
                        return <li key={index} className="disabled">{title}</li>;
                    }

                    return (
                        <li key={index} role="presentation">
                            <a href={this.hash(item)} className={item.className}>
                            {
                                item.icon && <i className={'fa fa-fw fa-' + item.icon} />
                            }
                            {
                                !!item.count && <Badge pullRight>{item.count}</Badge>
                            }
                            {item.title}
                            </a>
                        </li>
                        );
                })}
            </ul>
        );
    }
}


Sideview.propTypes = {
    views: React.PropTypes.array.isRequired,
    route: React.PropTypes.object.isRequired,
    queryParams: React.PropTypes.object
};

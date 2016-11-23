import SelectFilter from './select-filter';
import PeriodFilter from './period-filter';

/**
 * Create a new filter component by the given filter
 */
export default {
    create: filter => {
        switch (filter.type) {
            case 'select':
            case 'multi-select': return SelectFilter;
            case 'period': return PeriodFilter;
            default: return null;
        }
    }
};

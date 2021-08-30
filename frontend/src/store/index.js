import Vue from 'vue';
import Vuex from 'vuex';

import state from './state';
import mutations from './mutations';
import actions from './actions';
import getters from './getters';

import AuthModule from './modules/auth'
import UsersModule from './modules/users'
import RolesModule from './modules/roles'
import CategoriesModule from './modules/categories'
import InventoryModule from './modules/inventories'
import ChecksModule from './modules/checks'
import ChecksReportsModule from './modules/checks_reports'

Vue.use(Vuex);

const store = new Vuex.Store({
    state,
    mutations,
    actions,
    getters,
    modules: {
        AuthModule,
        UsersModule,
        RolesModule,
        CategoriesModule,
        InventoryModule,
        ChecksModule,
        ChecksReportsModule
    }
});

export default store;
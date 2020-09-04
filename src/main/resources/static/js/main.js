import Vue from 'vue'
import {BootstrapVue, IconsPlugin} from 'bootstrap-vue'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

var auctionApi = Vue.resource('/auction/all');

Vue.component('auction-element', {
  props: ['auction'],
  template: '<div><i>{{ auction.id }}</i> {{ auction.fileNumber }}</div>'
})

Vue.component('auction-list', {
  props: ['auctionsProperty'],
  template:
      '<div>' +
      '<auction-element v-for="auction in auctionsProperty" :key="auction.id" :auction="auction" />' +
      '</div>',
  created: function() {
    auctionApi.get().then(result =>
        result.json().then(data =>
            data.auctions.forEach(auction => this.auctionsProperty.push(auction))
        )
    );
  }
});

var app = new Vue({
  el: '#app',
  template: '<auction-list :auctionsProperty="auctions" />',
  data: {
    auctions: []
  }
});

// Install BootstrapVue
Vue.use(BootstrapVue)
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin)

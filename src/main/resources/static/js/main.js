var auctionApi = Vue.resource('/auction/all');

import moment from 'moment';

Vue.filter('formatDate', function(value) {
  if (value) {
    return moment(String(value)).format('MM/DD/YYYY hh:mm')
  }
});

Vue.component('auction-element', {
  props: ['auction'],
  template:
      '<div class="media">' +
      '<div class="fav-box"><i class="fa fa-heart-o" aria-hidden="true"></i></div>' +
      '<img class="d-flex align-self-start" src="https://images.pexels.com/photos/186077/pexels-photo-186077.jpeg?h=350&auto=compress&cs=tinysrgb" alt="Generic placeholder image">' +
      '<div class="media-body pl-3">' +
      '<div class="price">{{ auction.amount }}</div>' +
      '<div>{{ auction.zip_code + " " + auction.city }}</div>' +
      '<div>{{ auction.file_number }}</div>' +
      '<div class="stats">' +
      '<div><i class="fa fa-building-o"></i>{{ auction.property_type }}</div>' +
      '<div><i class="fa fa-arrows-alt"></i>100</div>' +
      '<div><i class="fa fa-clock-o"></i>{{ auction.appointment | formatDate }}</div>' +
      '</div>' +
      '<div class="address"><i class="fa fa-map"></i>{{ auction.street + " " + auction.number }}</div>' +
      '</div>' +
      '</div>'
})

Vue.component('auction-list', {
  props: ['auctionsProperty'],
  template:
      '<div class="row">' +
      '<div class="col-md-12 listing-block">' +
      '<auction-element v-for="auction in auctionsProperty" :key="auction.id" :auction="auction" />' +
      '</div>' +
      '</div>',
  created: function() {
    auctionApi.get().then(result =>
        result.json().then(data =>
            data.auction_previews.forEach(auction => this.auctionsProperty.push(auction))
        )
    );
  }
});

var app = new Vue({
  el: '#app',
  template: '<auction-list :auctionsProperty="auctions" />',
  data: {
    auction_previews: []
  }
});

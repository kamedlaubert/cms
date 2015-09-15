'use strict';

angular.module('cmsApp')
    .factory('OrdonnanceSearch', function ($resource) {
        return $resource('api/_search/ordonnances/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

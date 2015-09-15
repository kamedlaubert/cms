'use strict';

angular.module('cmsApp')
    .factory('ConjointSearch', function ($resource) {
        return $resource('api/_search/conjoints/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

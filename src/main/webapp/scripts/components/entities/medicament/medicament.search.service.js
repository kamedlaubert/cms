'use strict';

angular.module('cmsApp')
    .factory('MedicamentSearch', function ($resource) {
        return $resource('api/_search/medicaments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

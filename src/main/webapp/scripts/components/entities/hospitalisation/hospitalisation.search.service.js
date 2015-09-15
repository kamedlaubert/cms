'use strict';

angular.module('cmsApp')
    .factory('HospitalisationSearch', function ($resource) {
        return $resource('api/_search/hospitalisations/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

'use strict';

angular.module('cmsApp')
    .factory('MedecinSearch', function ($resource) {
        return $resource('api/_search/medecins/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

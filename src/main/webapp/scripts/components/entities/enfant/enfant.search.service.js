'use strict';

angular.module('cmsApp')
    .factory('EnfantSearch', function ($resource) {
        return $resource('api/_search/enfants/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

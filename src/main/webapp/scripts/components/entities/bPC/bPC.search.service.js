'use strict';

angular.module('cmsApp')
    .factory('BPCSearch', function ($resource) {
        return $resource('api/_search/bPCs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

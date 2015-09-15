'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('symptome', {
                parent: 'entity',
                url: '/symptomes',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.symptome.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/symptome/symptomes.html',
                        controller: 'SymptomeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('symptome');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('symptome.detail', {
                parent: 'entity',
                url: '/symptome/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.symptome.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/symptome/symptome-detail.html',
                        controller: 'SymptomeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('symptome');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Symptome', function($stateParams, Symptome) {
                        return Symptome.get({id : $stateParams.id});
                    }]
                }
            })
            .state('symptome.new', {
                parent: 'symptome',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/symptome/symptome-dialog.html',
                        controller: 'SymptomeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {libelle: null, description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('symptome', null, { reload: true });
                    }, function() {
                        $state.go('symptome');
                    })
                }]
            })
            .state('symptome.edit', {
                parent: 'symptome',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/symptome/symptome-dialog.html',
                        controller: 'SymptomeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Symptome', function(Symptome) {
                                return Symptome.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('symptome', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

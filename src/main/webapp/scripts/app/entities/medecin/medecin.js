'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('medecin', {
                parent: 'entity',
                url: '/medecins',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.medecin.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/medecin/medecins.html',
                        controller: 'MedecinController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('medecin');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('medecin.detail', {
                parent: 'entity',
                url: '/medecin/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.medecin.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/medecin/medecin-detail.html',
                        controller: 'MedecinDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('medecin');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Medecin', function($stateParams, Medecin) {
                        return Medecin.get({id : $stateParams.id});
                    }]
                }
            })
            .state('medecin.new', {
                parent: 'medecin',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/medecin/medecin-dialog.html',
                        controller: 'MedecinDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('medecin', null, { reload: true });
                    }, function() {
                        $state.go('medecin');
                    })
                }]
            })
            .state('medecin.edit', {
                parent: 'medecin',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/medecin/medecin-dialog.html',
                        controller: 'MedecinDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Medecin', function(Medecin) {
                                return Medecin.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('medecin', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

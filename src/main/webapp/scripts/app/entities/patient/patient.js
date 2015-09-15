'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('patient', {
                parent: 'entity',
                url: '/patients',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.patient.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/patient/patients.html',
                        controller: 'PatientController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('patient');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('patient.detail', {
                parent: 'entity',
                url: '/patient/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.patient.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/patient/patient-detail.html',
                        controller: 'PatientDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('patient');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Patient', function($stateParams, Patient) {
                        return Patient.get({id : $stateParams.id});
                    }]
                }
            })
            .state('patient.new', {
                parent: 'patient',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/patient/patient-dialog.html',
                        controller: 'PatientDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nom: null, prenom: null, dateNaissance: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('patient', null, { reload: true });
                    }, function() {
                        $state.go('patient');
                    })
                }]
            })
            .state('patient.edit', {
                parent: 'patient',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/patient/patient-dialog.html',
                        controller: 'PatientDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Patient', function(Patient) {
                                return Patient.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('patient', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

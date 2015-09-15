'use strict';

angular.module('cmsApp').controller('SymptomeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Symptome', 'Consultation',
        function($scope, $stateParams, $modalInstance, entity, Symptome, Consultation) {

        $scope.symptome = entity;
        $scope.consultations = Consultation.query();
        $scope.load = function(id) {
            Symptome.get({id : id}, function(result) {
                $scope.symptome = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:symptomeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.symptome.id != null) {
                Symptome.update($scope.symptome, onSaveFinished);
            } else {
                Symptome.save($scope.symptome, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
